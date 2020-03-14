package com.sicktracker;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAmount;
import java.util.Date;

public interface Clock {

    Clock SYSTEM = new SystemClock();

    LocalDateTime asDateTime(Date date);

    LocalDateTime dateTime();

    Date asDate(LocalDateTime dateTime);

    Date asDate(LocalDate date);

    LocalDate date();

    boolean isBetweenNowAnd(TemporalAmount threshold, LocalDateTime dateTime);

    LocalDate asDate(long epoch);

    final class SystemClock implements Clock {

        @Override
        public LocalDateTime asDateTime(Date date) {
            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        }

        @Override
        public LocalDateTime dateTime() {
            return LocalDateTime.now();
        }

        @Override
        public Date asDate(LocalDateTime dateTime) {
            return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
        }

        @Override
        public Date asDate(LocalDate date) {
            return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
        }

        @Override
        public LocalDate date() {
            return LocalDate.now();
        }

        @Override
        public boolean isBetweenNowAnd(TemporalAmount threshold, LocalDateTime dateTime) {
            LocalDateTime now = dateTime();
            return dateTime.isAfter(now) && dateTime.isBefore(now.plus(threshold));
        }

        @Override
        public LocalDate asDate(long epoch) {
            return LocalDate.ofInstant(Instant.ofEpochMilli(epoch), ZoneId.systemDefault());
        }

        @Override
        public Date now() {
            return new Date();
        }

    }

    Date now();

}
