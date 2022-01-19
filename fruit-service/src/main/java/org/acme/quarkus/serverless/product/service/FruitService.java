package org.acme.quarkus.serverless.product.service;

import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.acme.quarkus.serverless.product.model.FruityVice;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("api/fruit")
@RegisterRestClient
public interface FruitService {

    @GET
    @Path("{nutrition}")
    @Produces(MediaType.APPLICATION_JSON)
    @Retry(maxRetries = 10, delay = 1, delayUnit = ChronoUnit.SECONDS)
    List<FruityVice> getProducts(@PathParam("nutrition") String nutrition, @QueryParam("min") int min, @QueryParam("max") int max);

}
