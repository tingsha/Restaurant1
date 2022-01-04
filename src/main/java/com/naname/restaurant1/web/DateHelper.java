package com.naname.restaurant1.web;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DateHelper {
    public String getMinBookDate(){
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("America/New_York"));
        zonedDateTime = zonedDateTime.plusHours(6);
        return zonedDateTime.toString().substring(0, 16);
    }

    public String getMaxBookDate(){
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("America/New_York"));
        zonedDateTime = zonedDateTime.plusDays(10);
        return zonedDateTime.toString().substring(0, 16);
    }
}
