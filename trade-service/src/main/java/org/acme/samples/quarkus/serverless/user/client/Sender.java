package org.acme.samples.quarkus.serverless.user.client;

import org.acme.samples.quarkus.serverless.user.message.Order;
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
