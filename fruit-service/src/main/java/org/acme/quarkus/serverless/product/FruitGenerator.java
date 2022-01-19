package org.acme.quarkus.serverless.product;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.quarkus.runtime.Startup;
import org.acme.quarkus.serverless.product.model.FruityVice;
import org.acme.quarkus.serverless.product.model.Product;
import org.acme.quarkus.serverless.product.repository.ProductRepository;
import org.acme.quarkus.serverless.product.service.FruitService;

import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
@Startup
public class FruitGenerator {

    @RestClient
    FruitService service;

    @Inject
    ProductRepository repository;

    @PostConstruct
    public void init() {
        List<FruityVice> fruits = service.getProducts("calories", 0, 1000);
        List<Product> products = fruits.stream().map(fruit -> Product.of(fruit)).collect(Collectors.toList());
        repository.persist(products);
    }


}