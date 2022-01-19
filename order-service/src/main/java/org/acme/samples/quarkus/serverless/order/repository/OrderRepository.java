package org.acme.samples.quarkus.serverless.order.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.acme.samples.quarkus.serverless.order.model.Order;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.stream.Stream;

@ApplicationScoped
public class OrderRepository implements PanacheRepository<Order> {
    @Override
    @Transactional
    public void persist(Order order) {
        PanacheRepository.super.persist(order);
    }
}
