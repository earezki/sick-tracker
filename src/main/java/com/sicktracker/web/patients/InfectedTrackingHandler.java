package com.sicktracker.web.patients;

import com.sicktracker.application.Address;
import com.sicktracker.application.PatientService;
import com.sicktracker.patients.GeoLocation;
import com.sicktracker.patients.Latitude;
import com.sicktracker.patients.Longitude;
import io.reactivex.Single;
import io.vertx.core.Handler;
import io.vertx.reactivex.ext.web.RoutingContext;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isNotBlank;


public class InfectedTrackingHandler implements Handler<RoutingContext> {

    private final PatientService patientService;

    public InfectedTrackingHandler(PatientService patientService) {
        this.patientService = patientService;
    }

    @Override
    public void handle(RoutingContext context) {
        Single<Long> infected = infectedCount(context);

        infected
                .subscribe(count -> context.response().end(format("{\"infected\": %s}", count)), context::fail);
    }

    private Single<Long> infectedCount(RoutingContext context) {
        String address = context.request().getParam("address");
        if (isNotBlank(address)) {
            return patientService.infected(Address.of(address));
        }

        double latitude = Double.parseDouble(context.request().getParam("latitude"));
        double longitude = Double.parseDouble(context.request().getParam("longitude"));

        return patientService.infected(GeoLocation.of(Latitude.of(latitude), Longitude.of(longitude)));
    }

}
