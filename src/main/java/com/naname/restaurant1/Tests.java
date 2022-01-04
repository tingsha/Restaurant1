package com.naname.restaurant1;

import com.naname.restaurant1.web.DateHelper;

public class Tests {
    public static void main(String[] args) {
        System.out.println(new DateHelper().getMinBookDate());
        System.out.println(new DateHelper().getMaxBookDate());
    }
}
