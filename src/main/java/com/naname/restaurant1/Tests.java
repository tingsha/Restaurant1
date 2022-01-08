package com.naname.restaurant1;

import com.naname.restaurant1.web.DateHelper;

import java.time.ZoneId;

public class Tests {
    public static void main(String[] args) {
        System.out.println(new DateHelper(ZoneId.of("America/New_York")).getMinBookDate());
        System.out.println(new DateHelper(ZoneId.of("America/New_York")).getMaxBookDate());
    }
}
