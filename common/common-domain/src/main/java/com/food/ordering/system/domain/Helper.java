package com.food.ordering.system.domain;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static com.food.ordering.system.domain.DomainConstants.UTC_TIME_ZONE;

public class Helper {
    public static class DateTimeFactory {
        public static ZonedDateTime createUTCZonedDateTime() {
            return ZonedDateTime.now(ZoneId.of(UTC_TIME_ZONE));
        }
    }
}
