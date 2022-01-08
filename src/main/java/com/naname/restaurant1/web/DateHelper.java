package com.naname.restaurant1.web;

import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DateHelper {
    private final ZoneId zoneId;

    public DateHelper(ZoneId zoneId) {
        this.zoneId = zoneId;
    }

    public String getMinBookDate(){
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
        zonedDateTime = zonedDateTime.plusHours(6);
        return zonedDateTime.toString().substring(0, 16);
    }

    public String getMaxBookDate(){
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
        zonedDateTime = zonedDateTime.plusDays(10);
        return zonedDateTime.toString().substring(0, 16);
    }
}
