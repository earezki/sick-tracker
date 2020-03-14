package com.sicktracker.web;

import com.sicktracker.application.PatientService;
import com.sicktracker.patients.Username;
import io.vertx.core.Handler;
import io.vertx.reactivex.ext.web.RoutingContext;

import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;

public class HealedHandler implements Handler<RoutingContext> {

    private final PatientService patientService;

    public HealedHandler(PatientService patientService) {
        this.patientService = patientService;
    }

    @Override
    public void handle(RoutingContext context) {
        String username = context.pathParam("username");

        this.patientService.healed(Username.of(username))
                .subscribe(() -> context.response().setStatusCode(NO_CONTENT.code()), context::fail);
    }
}
