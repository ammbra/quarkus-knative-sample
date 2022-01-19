package org.acme.samples.quarkus.serverless.order.function;

import io.quarkus.funqy.Funq;
import org.acme.samples.quarkus.serverless.order.model.Order;
import org.acme.samples.quarkus.serverless.order.service.OrderService;
import org.acme.samples.quarkus.serverless.order.client.Sender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class OrderStore {

    Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Inject
    OrderService orderService;
    @Inject
    Sender sender;

    @Funq
    public void save(Order order) {
        LOGGER.info("Accepted order: {}", order);
        Order  o = orderService.save(order);
        if (o != null) {
            sender.send(o);
        }
    }

}
