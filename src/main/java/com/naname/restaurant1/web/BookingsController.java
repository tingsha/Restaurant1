package com.naname.restaurant1.web;

import com.naname.restaurant1.data.BookingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping(value = "/bookings")
public class BookingsController {

    private final BookingRepository bookingRepository;

    private class InputContent{
        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    @Autowired
    public BookingsController(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @GetMapping(value = "/page/{pageNumber}")
    public String getBookings(@PathVariable int pageNumber, Model model){
        log.info("isAdmin: " + model.getAttribute("isAdmin"));

        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("America/New_York"));
        Page<Booking> bookings = bookingRepository.findBookingsByDateAfter(zonedDateTime.toLocalDateTime(),
                PageRequest.of(pageNumber-1, 10, Sort.by(Sort.Direction.DESC, "id")));

        model.addAttribute("isLastPage", !bookings.hasNext());
        model.addAttribute("isFirstPage", !bookings.hasPrevious());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("bookings", bookings.getContent());
        return "bookings";
    }

    @PostMapping(value = "/search")
    public String findBookings(@ModelAttribute("input") InputContent inputContent, Model model){
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("America/New_York"));
        List<Booking> getBookingByName = bookingRepository.findBookingsByDateAfterAndName(zonedDateTime.toLocalDateTime(),
                inputContent.getContent());

        List<Booking> getBookingByEmail = bookingRepository.findBookingsByDateAfterAndEmail(zonedDateTime.toLocalDateTime(),
                inputContent.getContent());

        if (getBookingByName.size() > 0){
            model.addAttribute("bookings", getBookingByName);
        } else if(getBookingByEmail.size() > 0){
            model.addAttribute("bookings", getBookingByEmail);
        } else{
            model.addAttribute("bookings", new ArrayList<>());
        }

        model.addAttribute("isLastPage", true);
        model.addAttribute("isFirstPage", true);
        model.addAttribute("pageNumber", 0);

        return "bookings";
    }

    @GetMapping
    public String getBookings(){
        return "bookings";
    }

    @PatchMapping(value = "/confirm/{page}/{id}")
    public String confirmStatus(@PathVariable int page, @PathVariable int id){
        bookingRepository.updateStatus(id, "confirmed");
        return "redirect:/bookings/page/" + page;
    }

    @PatchMapping(value = "/cancel/{page}/{id}")
    public String cancelStatus(@PathVariable int page, @PathVariable int id){
        bookingRepository.updateStatus(id, "canceled");
        return "redirect:/bookings/page/" + page;
    }

    @PatchMapping(value = "/reset/{page}/{id}")
    public String resetStatus(@PathVariable int page, @PathVariable int id){
        bookingRepository.updateStatus(id, "waiting");
        return "redirect:/bookings/page/" + page;
    }

    @ModelAttribute("isAdmin")
    public boolean isAdmin(HttpServletRequest request){
        return request.isUserInRole("ROLE_ADMIN");
    }

    @ModelAttribute("input")
    public InputContent getInputContent(){
        return new InputContent();
    }
}
