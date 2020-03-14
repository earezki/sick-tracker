package com.sicktracker.web.patients;

import com.sicktracker.patients.*;

public class PatientTrackingDto {

    private String username;
    private double latitude;
    private double longitude;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    Patient toPatient() {
        return Patient.of(Username.of(username), GeoLocation.of(Latitude.of(latitude), Longitude.of(longitude)));
    }

}
