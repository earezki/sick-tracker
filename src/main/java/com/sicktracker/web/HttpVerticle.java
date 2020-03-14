package com.sicktracker.web;

import com.sicktracker.Clock;
import com.sicktracker.application.PatientService;
import com.sicktracker.web.patients.InfectedTrackingHandler;
import com.sicktracker.web.patients.PatientTrackingHandler;
import com.sicktracker.web.toolkit.FailureHandler;
import com.sicktracker.web.toolkit.Mapping;
import com.sicktracker.web.toolkit.ServerAccessHandler;
import io.reactivex.disposables.Disposable;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.handler.BodyHandler;

import java.util.ArrayList;
import java.util.List;


public class HttpVerticle extends AbstractVerticle {

    private final PatientService patientService;
    private final Mapping mapping;
    private final Clock clock;
    private final int port;

    private final List<Disposable> disposables = new ArrayList<>();

    public HttpVerticle(PatientService patientService, Mapping mapping, Clock clock, int port) {
        this.patientService = patientService;
        this.mapping = mapping;
        this.clock = clock;
        this.port = port;
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        Router router = Router.router(vertx);
        router.route().handler(new ServerAccessHandler());

        router.route().failureHandler(new FailureHandler());

        router.route().handler(new CommonHeadersHandler());
        router.route()
                .method(HttpMethod.PATCH)
                .method(HttpMethod.PUT)
                .method(HttpMethod.POST)
                .handler(BodyHandler.create());

        router.route("/probs")
                .method(HttpMethod.GET)
                .handler(new ProbEndpoint());

        router.route("/patients")
                .method(HttpMethod.POST)
                .handler(new PatientTrackingHandler(patientService, mapping));

        router.route("/patients/:username")
                .method(HttpMethod.DELETE)
                .handler(new HealedHandler(patientService));

        router.route("/patients")
                .method(HttpMethod.GET)
                .handler(new InfectedTrackingHandler(patientService));

        final HttpServer server = vertx.createHttpServer();
        disposables.add(
                server.requestHandler(router)
                        .rxListen(port)
                        .subscribe(httpServer -> startPromise.complete(), startPromise::fail)
        );

    }

    @Override
    public void stop(Promise<Void> stopPromise) throws Exception {
        disposables.forEach(Disposable::dispose);
        stopPromise.complete();
    }
}
