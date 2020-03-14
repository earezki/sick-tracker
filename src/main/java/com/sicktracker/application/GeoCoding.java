package com.sicktracker.application;

import com.sicktracker.patients.GeoLocation;
import io.reactivex.Single;

public interface GeoCoding {

    Single<GeoLocation> from(Address address);

}
