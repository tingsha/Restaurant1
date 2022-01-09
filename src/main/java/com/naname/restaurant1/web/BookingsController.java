package com.naname.restaurant1.web;

import com.naname.restaurant1.data.BookingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Slf4j
@Controller
@RequestMapping(value = "/bookings")
public class BookingsController {

    private final BookingRepository bookingRepository;

    @Autowired
    public BookingsController(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @GetMapping(value = "/page/{pageNumber}")
    public String getBookings(@PathVariable int pageNumber, HttpServletRequest request, Model model){
        model.addAttribute("isAdmin", request.isUserInRole("ROLE_ADMIN"));
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
}
