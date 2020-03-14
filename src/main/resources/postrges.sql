CREATE DATABASE PATIENTS_TRACKING;
CREATE TABLE PATIENTS (
    USERNAME varchar(64),
    LATITUDE double precision,
    LONGITUDE double precision,
    GEO_HASH varchar(12),
    PRIMARY KEY (USERNAME)
);
CREATE INDEX geo_hash_index ON patients(geo_hash);