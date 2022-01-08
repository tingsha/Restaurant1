package com.naname.restaurant1.data;

import com.naname.restaurant1.web.Booking;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

public interface BookingRepository extends CrudRepository<Booking, Integer> {
    @Query(value = "SELECT * FROM reservations " +
            "WHERE email = ?1 " +
            "AND EXTRACT(DAY FROM date) = ?2 " +
            "AND EXTRACT(MONTH FROM date) = ?3",
        nativeQuery = true)
    Booking findByEmailAndDate(String email, int day, int month);

    List<Booking> findBookingsByDateAfter(LocalDateTime date);
}
