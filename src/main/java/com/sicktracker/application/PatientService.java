package com.sicktracker.application;

import com.sicktracker.patients.GeoLocation;
import com.sicktracker.patients.Patient;
import com.sicktracker.patients.Username;
import io.reactivex.Completable;
import io.reactivex.Single;

import java.util.concurrent.Flow;

public class PatientService {

    private final PatientRepository patientRepository;
    private final GeoCoding geoCoding;

    public PatientService(PatientRepository patientRepository, GeoCoding geoCoding) {
        this.patientRepository = patientRepository;
        this.geoCoding = geoCoding;
    }

    public Completable track(Patient patient) {
        return patientRepository.findByUsername(patient.username())
                .switchIfEmpty(Single.defer(() -> patientRepository.store(patient).toSingle(() -> patient)))
                .flatMapCompletable(existingPatient -> {
                    if (existingPatient.haveChangedLocation(patient)) {
                        return patientRepository.update(patient);
                    }
                    return Completable.complete();
                });
    }

    public Single<Long> infected(Address address) {
        return geoCoding.from(address).flatMap(this::infected);
    }

    public Single<Long> infected(GeoLocation geoLocation) {
        return patientRepository.countByGeoHash(geoLocation.hash());
    }

    public Completable healed(Username username) {
        return patientRepository.delete(username);
    }
}
