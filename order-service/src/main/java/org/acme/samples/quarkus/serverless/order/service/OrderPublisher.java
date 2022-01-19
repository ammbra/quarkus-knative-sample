package org.acme.samples.quarkus.serverless.order.service;

import io.smallrye.mutiny.Multi;
import io.smallrye.reactive.messaging.annotations.Broadcast;
import org.acme.samples.quarkus.serverless.order.model.Order;
import org.acme.samples.quarkus.serverless.order.model.OrderStatus;
import org.acme.samples.quarkus.serverless.order.repository.OrderRepository;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.transaction.UserTransaction;
import java.time.Duration;

@ApplicationScoped
public class OrderPublisher {

    private static long num = 0;

    Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Inject
    OrderRepository repository;

    @ConfigProperty(name = "app.orders.timeout")
    int timeout;

    @Outgoing("order-events")
    @Broadcast
    public Multi<Order> orderEventsPublish() {
        return Multi.createFrom().ticks().every(Duration.ofMillis(timeout))
                .map(tick -> {
                    ++num;
                    Order order = new Order((int) num%1000+1, (int) num%100+1, 100, 1, OrderStatus.NEW);
                    try {
                        repository.persist(order);
                    } catch (Exception e) {
                        LOGGER.error("Error in transaction {}", e);
                    }

                    LOGGER.info("Order published:{}", order);
                    return order;
                });
    }

}
