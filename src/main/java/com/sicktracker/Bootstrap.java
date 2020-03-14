package com.sicktracker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sicktracker.application.PatientService;
import com.sicktracker.infrastructure.ApiKey;
import com.sicktracker.infrastructure.GoogleGeoCoding;
import com.sicktracker.persistence.PostgresPatientRepository;
import com.sicktracker.web.HttpVerticle;
import com.sicktracker.web.toolkit.Mapping;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Slf4JLoggerFactory;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.VertxOptions;
import io.vertx.core.dns.AddressResolverOptions;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.client.WebClient;
import io.vertx.reactivex.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class Bootstrap {

    private static final Logger LOG = LoggerFactory.getLogger(Bootstrap.class);


    public static void main(String[] args) throws IOException {
        System.setProperty("vertx.logger-delegate-factory-class-name", "io.vertx.core.logging.SLF4JLogDelegateFactory");
        InternalLoggerFactory.setDefaultFactory(Slf4JLoggerFactory.INSTANCE);

        Properties properties = properties(args);
        Config config = new Config(properties);

        Vertx vertx = Vertx.vertx(new VertxOptions()
                .setBlockedThreadCheckInterval(config.getInt("vertx.blocking.check.interval")));


        PgConnectOptions connectOptions = new PgConnectOptions()
                .setPort(config.getInt("postgres.port"))
                .setHost(config.getString("postgres.host"))
                .setDatabase(config.getString("postgres.database"))
                .setUser(config.getString("postgres.username"))
                .setPassword(config.getString("postgres.password"))
                .setCachePreparedStatements(true);

        PoolOptions poolOptions = new PoolOptions()
                .setMaxSize(config.getInt("postgres.pool.size"));

        PgPool pool = PgPool.pool(vertx, connectOptions, poolOptions);

        WebClient webClient = WebClient.create(vertx, new WebClientOptions()
                .setKeepAlive(true));
        vertx.rxDeployVerticle(() -> new HttpVerticle(new PatientService(new PostgresPatientRepository(pool),
                        new GoogleGeoCoding(webClient, ApiKey.of(config.getString("google.maps.key")),
                                config.getLong("google.geodecoding.timeout.in.seconds"))),
                        new Mapping(new ObjectMapper()),
                        Clock.SYSTEM, config.getInt("server.port.http")),
                new DeploymentOptions()
                        .setInstances(Runtime.getRuntime().availableProcessors() * 2 + 1))
                .subscribe(result -> LOG.info("App successfully started: {}", result));

    }

    private static Properties properties(String[] args) throws IOException {
        Properties properties = new Properties();
        if (args != null && args.length > 0) {
            properties.load(Files.newInputStream(Paths.get(args[0])));
        } else {
            properties.load(Bootstrap.class.getResourceAsStream("/application.properties"));
        }
        return properties;
    }

}
