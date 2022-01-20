package org.acme.quarkus.serverless.product.function;

import io.quarkus.funqy.Funq;
import org.acme.quarkus.serverless.product.message.Order;
import org.acme.quarkus.serverless.product.message.OrderStatus;
import org.acme.quarkus.serverless.product.repository.ProductRepository;
import org.acme.quarkus.serverless.product.client.Sender;
import org.acme.quarkus.serverless.product.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class FruitProcessor {

    Logger LOGGER = LoggerFactory.getLogger(getClass());

    private static final String SOURCE = "fruit";


    @Inject
    ProductRepository repository;

    @Inject
    Sender sender;

    @Funq
    public void process(Order order) {
        LOGGER.info("Received order: {}", order);
        if (order.getStatus() == OrderStatus.NEW)
            save(order);
        else
            confirm(order);
    }

    private void save(Order order) {
        Product product = repository.findById(order.getProductId());
        double total = 0;
        LOGGER.info("Product: {}", product);
        if (order.getStatus() == OrderStatus.NEW) {
            if (order.getProductsCount() < product.getAvailableItems()) {
                product.setReservedItems(product.getReservedItems() + order.getProductsCount());
                product.setAvailableItems(product.getAvailableItems() - order.getProductsCount());
                order.setStatus(OrderStatus.IN_PROGRESS);
                total += product.getCalories();
                repository.persist(product);
            } else {
                order.setStatus(OrderStatus.REJECTED);
            }
            order.setTotalCalories(total);
            order.setSource(SOURCE);
            LOGGER.debug("Order saved: {}", order);
            sender.send(order);
        }
    }

    private void confirm(Order order) {
        Product product = repository.findById(order.getProductId());
        LOGGER.info("Product:{}", product);
        product.setReservedItems(product.getReservedItems() - order.getProductsCount());

        if (order.getStatus() == OrderStatus.CANCELLED && order.getRejected() != null && !order.getRejected().equals(SOURCE)) {
            product.setAvailableItems(product.getAvailableItems() + order.getProductsCount());
        }
        repository.persist(product);

    }
}
