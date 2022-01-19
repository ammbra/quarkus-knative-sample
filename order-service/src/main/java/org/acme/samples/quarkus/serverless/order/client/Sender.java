package org.acme.samples.quarkus.serverless.order.client;

import org.acme.samples.quarkus.serverless.order.model.Order;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class Sender {

    @Inject
    @Channel("order-events")
    Emitter<Order> emitter;

    public void send(Order order) {
        emitter.send(order);
    }

}
