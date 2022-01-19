package org.acme.samples.quarkus.serverless.user;

import io.quarkus.runtime.Startup;
import org.acme.samples.quarkus.serverless.user.repository.CustomerRepository;
import org.acme.samples.quarkus.serverless.user.model.Customer;


import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@ApplicationScoped
@Startup
public class CustomerGenerator {

    private final Random r = new Random();

    @Inject
    CustomerRepository repository;

    @PostConstruct
    void init() {
        List<Customer> users = new ArrayList<>(100);
        for (long i = 0; i < 100; i++) {
            users.add(new Customer("User" + i, r.nextInt(100000) + 1000, 0));
        }
        repository.persist(users);
    }
}
