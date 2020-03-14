package com.sicktracker.web;

import io.vertx.core.Handler;
import io.vertx.reactivex.ext.web.RoutingContext;

public class CommonHeadersHandler implements Handler<RoutingContext> {

    @Override
    public void handle(RoutingContext context) {
        context.response()
                .putHeader("Content-Type", "application/json; charset=utf-8")
                .putHeader("X-Content-Type-Options", "nosniff")
                .putHeader("Strict-Transport-Security", "max-age=15768000")
                .putHeader("X-Download-Options", "noopen")
                .putHeader("X-XSS-Protection", "1; mode=block")
                .putHeader("X-FRAME-OPTIONS", "DENY");

        context.next();
    }

}
