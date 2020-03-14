package com.sicktracker.patients;

import static org.apache.commons.lang3.Validate.notNull;

public class GeoLocation {

    private static final int MAX_LATITUDE = 90;
    private static final int MAX_LONGITUDE = 180;
    private static final String BASE_32 = "0123456789bcdefghjkmnpqrstuvwxyz";
    private static final Precision PRECISION = Precision.of(7);
    private final Latitude latitude;
    private final Longitude longitude;

    private GeoLocation(Latitude latitude, Longitude longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static GeoLocation of(Latitude latitude, Longitude longitude) {
        return new GeoLocation(notNull(latitude), notNull(longitude));
    }

    public GeoHash hash() {
        int idx = 0; // index into base32 map
        int bit = 0; // each char holds 5 bits
        StringBuilder geohash = new StringBuilder();

        double minLatitude = -MAX_LATITUDE, maxLatitude = MAX_LATITUDE;
        double minLongitude = -MAX_LONGITUDE, maxLongitude = MAX_LONGITUDE;

        for (int i = 0; i < PRECISION.size(); i++) {
            if (i % 2 == 0) {
                // bisect E-W longitude medium
                double longitudeMedium = (minLongitude + maxLongitude) / 2;
                if (longitude.isToTheRightTo(longitudeMedium)) {
                    idx = idx * 2 + 1;
                    minLongitude = longitudeMedium;
                } else {
                    idx = idx * 2;
                    maxLongitude = longitudeMedium;
                }
            } else {
                // bisect N-S latitude
                double latitudeMedium = (minLatitude + maxLatitude) / 2;
                if (latitude.isAbove(latitudeMedium)) {
                    idx = idx * 2 + 1;
                    minLatitude = latitudeMedium;
                } else {
                    idx = idx * 2;
                    maxLatitude = latitudeMedium;
                }
            }

            if (++bit == 5) {
                // 5 bits gives us a character: append it and start over
                geohash.append(BASE_32.charAt(idx));
                bit = 0;
                idx = 0;
            }
        }

        return GeoHash.of(geohash.toString());
    }

    public Latitude latitude() {
        return latitude;
    }

    public Longitude longitude() {
        return longitude;
    }
}
