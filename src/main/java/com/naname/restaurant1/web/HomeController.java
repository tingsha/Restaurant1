package com.naname.restaurant1.web;

import com.naname.restaurant1.data.BookingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping(value = "/")
public class HomeController {

    private final FieldValidator fieldValidator;
    private final BookingRepository bookingRepository;

    @Autowired
    public HomeController(FieldValidator fieldValidator, BookingRepository bookingRepository){
        this.fieldValidator = fieldValidator;
        this.bookingRepository = bookingRepository;
    }

    @GetMapping
    public String getIndex(@ModelAttribute("booking") Booking booking, Model model){
        model.addAttribute("booking", new Booking());
        return "index";
    }

    @PostMapping
    public @ResponseBody Map<String, Boolean> validateForms(@RequestBody Booking booking){
        booking.setStatus("waiting");
        log.info(booking.toString());

        Map<String, Boolean> json = new HashMap<>();
        json.put("isNameValid", fieldValidator.checkName(booking.getName()));
        json.put("isEmailValid", fieldValidator.checkEmail(booking.getEmail()));
        json.put("isTimeValid", fieldValidator.checkTime(booking.getDate()));
        json.put("isPartyNumberValid", fieldValidator.checkPartyNumber(booking.getPartyNumber()));
        json.put("notSpam", fieldValidator.checkSpam(booking.getEmail(), booking.getDate()));

        boolean isAllFieldsValid = true;
        for (Map.Entry<String, Boolean> entry : json.entrySet()) {
            log.info(entry.getKey() + " " + entry.getValue());
            if (!entry.getValue())
                isAllFieldsValid = false;
        }

        if (isAllFieldsValid){
            bookingRepository.save(booking);
        }
        return json;
    }

    @ModelAttribute("dateHelper")
    public DateHelper getDateHelper(){
        return new DateHelper(ZoneId.of("America/New_York"));
    }
}
