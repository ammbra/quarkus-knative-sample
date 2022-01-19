package org.acme.samples.quarkus.serverless.user.function;

import io.quarkus.funqy.Funq;
import org.acme.samples.quarkus.serverless.user.message.Order;
import org.acme.samples.quarkus.serverless.user.message.OrderStatus;
import org.acme.samples.quarkus.serverless.user.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.acme.samples.quarkus.serverless.user.client.Sender;
import org.acme.samples.quarkus.serverless.user.model.Customer;

import javax.inject.Inject;

public class TradeProcessor {

    Logger LOGGER = LoggerFactory.getLogger(getClass());

    private static final String SOURCE = "trade";

    @Inject
    CustomerRepository repository;

    @Inject
    Sender sender;

    @Funq
    public void trade(Order order) {
        LOGGER.info("Received order: {}", order);
        if (order.getStatus() == OrderStatus.NEW)
            reserve(order);
        else
            confirm(order);
    }

    private void reserve(Order order) {
        Customer user = repository.findById(order.getCustomerId());
        if (user == null)
            throw new RuntimeException("User not found");
        LOGGER.info("User: {}", user);
        if (order.getAmount() < user.getAmountAvailable()) {
            order.setStatus(OrderStatus.IN_PROGRESS);
            user.setAmountReserved(user.getAmountReserved() + order.getAmount());
            user.setAmountAvailable(user.getAmountAvailable() - order.getAmount());
        } else {
            order.setStatus(OrderStatus.REJECTED);
        }
        order.setSource(SOURCE);
        repository.persist(user);
        LOGGER.info("Order sent: {}", order);
        sender.send(order);
    }

    private void confirm(Order order) {
        Customer user = repository.findById(order.getCustomerId());
        if (user == null)
            throw new RuntimeException("User not found");
        LOGGER.info("Customer: {}", user);
        user.setAmountReserved(user.getAmountReserved() - order.getAmount());

        if (order.getStatus() == OrderStatus.CANCELLED && !order.getRejectedService().equals(SOURCE)) {
            user.setAmountAvailable(user.getAmountAvailable() + order.getAmount());
        }
        repository.persist(user);
    }
}
