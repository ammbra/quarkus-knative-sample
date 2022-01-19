package org.acme.samples.quarkus.serverless.user.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.acme.samples.quarkus.serverless.user.model.Customer;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
public class CustomerRepository implements PanacheRepository<Customer> {
    @Override
    @Transactional
    public void persist(Iterable<Customer> users) {
        PanacheRepository.super.persist(users);
    }
}
