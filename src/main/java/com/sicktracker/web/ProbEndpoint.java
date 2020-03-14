package com.sicktracker.web;

import io.vertx.core.Handler;
import io.vertx.reactivex.ext.web.RoutingContext;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;

public class ProbEndpoint implements Handler<RoutingContext> {

    @Override
    public void handle(RoutingContext context) {
        context.response().setStatusCode(OK.code()).end();
    }
}
