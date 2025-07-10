package com.hannah.AI_service.service;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

@Component
public class DateTimeFormatter {
    public String format(Instant instant){
        long elapseSeconds = ChronoUnit.SECONDS.between(instant, Instant.now());
        if(elapseSeconds < 60){
            return elapseSeconds + " Seconds";
        }
        else if(elapseSeconds < 3600){
            long elapseMinutes = ChronoUnit.MINUTES.between(instant, Instant.now());
            return elapseMinutes + " Minutes";
        }
        else if(elapseSeconds < 86400){
            long elapseHours = ChronoUnit.HOURS.between(instant, Instant.now());
            return elapseHours + " Hours";
        }
        LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        java.time.format.DateTimeFormatter dateTimeFormatter = java.time.format.DateTimeFormatter.ISO_DATE;
        return localDateTime.format(dateTimeFormatter);
    }
}
