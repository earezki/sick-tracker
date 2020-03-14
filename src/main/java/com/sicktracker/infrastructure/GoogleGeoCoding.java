package com.sicktracker.infrastructure;

import com.sicktracker.application.Address;
import com.sicktracker.application.GeoCoding;
import com.sicktracker.patients.GeoLocation;
import com.sicktracker.patients.Latitude;
import com.sicktracker.patients.Longitude;
import io.reactivex.Single;
import io.vertx.reactivex.ext.web.client.HttpResponse;
import io.vertx.reactivex.ext.web.client.WebClient;
import io.vertx.reactivex.ext.web.codec.BodyCodec;

import java.util.concurrent.TimeUnit;

public class GoogleGeoCoding implements GeoCoding {

    private static final int DEFAULT_HTTPS_PORT = 443;
    private static final int FIRST_RESULT = 0;

    private final WebClient webClient;
    private final ApiKey apiKey;
    private final long timeoutInSeconds;

    public GoogleGeoCoding(WebClient webClient, ApiKey apiKey, long timeoutInSeconds) {
        this.webClient = webClient;
        this.apiKey = apiKey;
        this.timeoutInSeconds = timeoutInSeconds;
    }

    @Override
    public Single<GeoLocation> from(Address address) {
        return webClient.getAbs("https://maps.googleapis.com/maps/api/geocode/json")
                .timeout(TimeUnit.SECONDS.toMillis(timeoutInSeconds))
                .addQueryParam("address", address.asString())
                .addQueryParam("key", apiKey.asString())
                .as(BodyCodec.jsonObject())
                .rxSend()
                .map(HttpResponse::body)
                .filter(body -> "OK".equalsIgnoreCase(body.getString("status")))
                .switchIfEmpty(Single.error(GeoLocationNotFoundException::new))
                .map(body -> body.getJsonArray("results")
                        .getJsonObject(FIRST_RESULT)
                        .getJsonObject("geometry")
                        .getJsonObject("location"))
                .map(response -> GeoLocation.of(Latitude.of(response.getDouble("lat")),
                        Longitude.of(response.getDouble("lng"))));
    }

}
