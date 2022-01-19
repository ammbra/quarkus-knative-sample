package org.acme.samples.quarkus.serverless.order.service;

import org.acme.samples.quarkus.serverless.order.model.Order;
import org.acme.samples.quarkus.serverless.order.model.OrderStatus;
import org.acme.samples.quarkus.serverless.order.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.LockModeType;
import javax.transaction.Transactional;

@ApplicationScoped
public class OrderService {

    Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Inject
    OrderRepository repository;

    @Transactional
    public Order save(Order o) {
        Order order = repository.findById(o.id, LockModeType.PESSIMISTIC_WRITE);
        if (order == null) {
            return null;
        } else if (order.getStatus() == OrderStatus.NEW) {
            order.setStatus(o.getStatus());
            if (o.getStatus() == OrderStatus.REJECTED)
                order.setRejectedService(o.getSource());
            return null;
        } else if (order.getStatus() == OrderStatus.IN_PROGRESS) {
            if (o.getStatus() == OrderStatus.REJECTED)
                order.setStatus(OrderStatus.CANCELLED);
            else
                order.setStatus(OrderStatus.CONFIRMED);
            LOGGER.info("Order confirmed: {}}", order);
        } else if (order.getStatus() == OrderStatus.REJECTED) {
            order.setStatus(OrderStatus.CANCELLED);
            order.setRejectedService(o.getSource());
            LOGGER.info("Order rejected: {}}", order);
        }
        repository.persist(order);
        return order;
    }
}
