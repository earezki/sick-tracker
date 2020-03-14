package com.sicktracker.web.toolkit;

import com.google.common.collect.ImmutableMap;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.reactivex.core.http.HttpServerResponse;
import io.vertx.reactivex.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static io.netty.handler.codec.http.HttpResponseStatus.INTERNAL_SERVER_ERROR;

public class FailureHandler implements Handler<RoutingContext> {

    private static final Logger LOG = LoggerFactory.getLogger(FailureHandler.class);

    private final Map<Class<? extends Throwable>, HttpResponseStatus> statusByError = ImmutableMap.<Class<? extends Throwable>, HttpResponseStatus>builder()
            .build();

    @Override
    public void handle(RoutingContext context) {
        Throwable throwable = context.failure();
        LOG.error("Error handling request {}", context.request().path(), throwable);

        HttpResponseStatus status = status(throwable);
        HttpServerResponse response = context.response()
                .setStatusCode(status.code());

        response.end();
    }


    private HttpResponseStatus status(Throwable throwable) {
        return statusByError.entrySet().stream()
                .filter(entry -> entry.getKey() == throwable.getClass())
                .map(Map.Entry::getValue)
                .findAny()
                .orElse(INTERNAL_SERVER_ERROR);
    }

}
