package com.sicktracker.persistence;

import com.sicktracker.application.PatientRepository;
import com.sicktracker.patients.*;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.vertx.reactivex.pgclient.PgPool;
import io.vertx.reactivex.sqlclient.Row;
import io.vertx.reactivex.sqlclient.Tuple;

import java.util.stream.StreamSupport;

public class PostgresPatientRepository implements PatientRepository {

    private final PgPool pool;

    public PostgresPatientRepository(PgPool pool) {
        this.pool = pool;
    }

    @Override
    public Completable store(Patient patient) {
        return pool.rxPreparedQuery("INSERT INTO PATIENTS (USERNAME, LATITUDE, LONGITUDE, GEO_HASH) VALUES ($1, $2, $3, $4)",
                Tuple.of(patient.username().asString(),
                        patient.geoLocation().latitude().asDecimal(),
                        patient.geoLocation().longitude().asDecimal(),
                        patient.geoHash().asString()))
                .ignoreElement();
    }

    @Override
    public Completable update(Patient patient) {
        return pool.rxPreparedQuery("UPDATE PATIENTS SET LATITUDE = $1, LONGITUDE = $2, GEO_HASH = $3 WHERE USERNAME = $4",
                Tuple.of(patient.geoLocation().latitude().asDecimal(),
                        patient.geoLocation().longitude().asDecimal(),
                        patient.geoHash().asString(),
                        patient.username().asString()))
                .ignoreElement();
    }

    @Override
    public Maybe<Patient> findByUsername(Username username) {
        return pool.rxPreparedQuery("SELECT USERNAME, LATITUDE, LONGITUDE FROM PATIENTS WHERE USERNAME = $1", Tuple.of(username.asString()))
                .flatMapMaybe(rows -> StreamSupport.stream(rows.spliterator(), false)
                        .findAny()
                        .map(Maybe::just)
                        .orElse(Maybe.empty()))
                .map(this::fromRow);
    }

    @Override
    public Single<Long> countByGeoHash(GeoHash hash) {
        return pool.rxPreparedQuery("SELECT COUNT(USERNAME) FROM PATIENTS WHERE GEO_HASH = $1", Tuple.of(hash.asString()))
                .map(rows -> StreamSupport.stream(rows.spliterator(), false)
                        .findAny()
                        .map(row -> row.getLong(0))
                        .orElse(0L));
    }

    @Override
    public Completable delete(Username username) {
        return pool.rxPreparedQuery("DELETE FROM PATIENTS WHERE username = $1", Tuple.of(username.asString()))
                .ignoreElement();

    }

    private Patient fromRow(Row row) {
        return Patient.of(Username.of(row.getString("username")),
                GeoLocation.of(Latitude.of(row.getDouble("latitude")),
                        Longitude.of(row.getDouble("longitude"))));
    }

}
