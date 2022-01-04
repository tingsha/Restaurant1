package com.naname.restaurant1.web;

import com.naname.restaurant1.data.BookingRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.*;

@Component
public class FieldValidator {
    private final BookingRepository bookingRepository;

    @Autowired
    public FieldValidator(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public boolean checkName(String name){
        return name.trim().length() != 0;
    }

    public boolean checkEmail(String email){
        return EmailValidator.getInstance().isValid(email);
    }

    public boolean checkPartyNumber(String partyNumber){
        return !partyNumber.equals("placeholder");
    }

    public boolean checkSpam(String email, LocalDateTime dateTime){
        try {
            int day = bookingRepository.findByEmailAndDate(email,
                            dateTime.getDayOfMonth(),
                            dateTime.getMonth().getValue())
                    .getDate().getDayOfMonth();
            Month month = bookingRepository.findByEmailAndDate(email,
                            dateTime.getDayOfMonth(),
                            dateTime.getMonth().getValue())
                    .getDate().getMonth();
            return !(day == dateTime.getDayOfMonth() && month == dateTime.getMonth());
        } catch (Exception e) {
            return true;
        }
    }

    public boolean checkTime(LocalDateTime dateTime){
        LocalTime localTime = LocalTime.of(dateTime.getHour(), dateTime.getMinute());
        if (dateTime.getDayOfWeek() == DayOfWeek.FRIDAY ||
                dateTime.getDayOfWeek() == DayOfWeek.SATURDAY ||
                dateTime.getDayOfWeek() == DayOfWeek.SUNDAY){
            return localTime.isBefore(LocalTime.of(22, 0)) && localTime.isAfter(LocalTime.of(7, 0));
        } else
            return localTime.isBefore(LocalTime.of(20, 0)) && localTime.isAfter(LocalTime.of(7, 0));
    }
}
