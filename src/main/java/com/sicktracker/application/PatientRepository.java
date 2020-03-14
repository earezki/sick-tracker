package com.sicktracker.application;

import com.sicktracker.patients.GeoHash;
import com.sicktracker.patients.Patient;
import com.sicktracker.patients.Username;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public interface PatientRepository {

    Completable store(Patient patient);

    Completable update(Patient patient);

    Maybe<Patient> findByUsername(Username username);

    Single<Long> countByGeoHash(GeoHash hash);

    Completable delete(Username username);
}
