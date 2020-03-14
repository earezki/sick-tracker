package com.sicktracker.web.toolkit;

import io.vertx.core.Handler;
import io.vertx.reactivex.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerAccessHandler implements Handler<RoutingContext> {

    private static final Logger LOG = LoggerFactory.getLogger(ServerAccessHandler.class);

    @Override
    public void handle(RoutingContext context) {
        long time = System.currentTimeMillis();
        context.addBodyEndHandler(ignoredVoid -> LOG.info("{} {} {} {} {}",
                context.request().path(),
                context.response().getStatusCode(),
                responseTime(time),
                context.request().bytesRead(),
                context.response().bytesWritten()));
        context.next();
    }

    private long responseTime(long time) {
        return System.currentTimeMillis() - time;
    }

}
