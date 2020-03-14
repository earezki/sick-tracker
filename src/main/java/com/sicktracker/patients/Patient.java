package com.sicktracker.patients;

import static org.apache.commons.lang3.Validate.notNull;

public class Patient {

    private final Username username;
    private final GeoLocation geoLocation;

    private Patient(Username username, GeoLocation geoLocation) {
        this.username = username;
        this.geoLocation = geoLocation;
    }

    public static Patient of(Username username, GeoLocation geoLocation) {
        return new Patient(notNull(username), notNull(geoLocation));
    }

    public Username username() {
        return username;
    }

    public boolean haveChangedLocation(Patient rhs) {
        return !geoLocation.hash().isSameAs(rhs.geoLocation.hash());
    }

    public GeoHash geoHash() {
        return geoLocation.hash();
    }

    public GeoLocation geoLocation() {
        return geoLocation;
    }
}
