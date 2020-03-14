package com.sicktracker.web.patients;

import com.sicktracker.application.PatientService;
import com.sicktracker.web.toolkit.Mapping;
import io.vertx.core.Handler;
import io.vertx.reactivex.ext.web.RoutingContext;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;

public class PatientTrackingHandler implements Handler<RoutingContext> {

    private final PatientService patientService;
    private final Mapping mapping;

    public PatientTrackingHandler(PatientService patientService, Mapping mapping) {
        this.patientService = patientService;
        this.mapping = mapping;
    }

    @Override
    public void handle(RoutingContext context) {
        PatientTrackingDto patientTracking = mapping.fromJson(context.getBodyAsString(), PatientTrackingDto.class);

        patientService.track(patientTracking.toPatient())
                .subscribe(() -> context.response()
                        .setStatusCode(OK.code())
                        .end(), context::fail);
    }

}
