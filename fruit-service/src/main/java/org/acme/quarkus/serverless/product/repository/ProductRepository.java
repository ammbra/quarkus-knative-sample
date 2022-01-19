package org.acme.quarkus.serverless.product.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.acme.quarkus.serverless.product.model.Product;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;


@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product> {

    @Override
    @Transactional
    public void persist(Iterable<Product> products) {
        PanacheRepository.super.persist(products);
    }
}
