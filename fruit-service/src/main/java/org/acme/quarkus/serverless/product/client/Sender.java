package org.acme.quarkus.serverless.product.client;

import org.acme.quarkus.serverless.product.message.Order;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class Sender {

    @Inject
    @Channel("trade-events")
    Emitter<Order> emitter;

    public void send(Order order) {
        emitter.send(order);
    }

}
