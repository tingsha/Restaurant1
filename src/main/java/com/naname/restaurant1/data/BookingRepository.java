package com.naname.restaurant1.data;

import com.naname.restaurant1.web.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends PagingAndSortingRepository<Booking, Integer> {

    @Query(value = "SELECT * FROM reservations " +
            "WHERE email = ?1 " +
            "AND EXTRACT(DAY FROM date) = ?2 " +
            "AND EXTRACT(MONTH FROM date) = ?3",
        nativeQuery = true)
    Booking findByEmailAndDate(String email, int day, int month);

    Page<Booking> findBookingsByDateAfter(LocalDateTime date, Pageable page);

    List<Booking> findBookingsByDateAfterAndName(LocalDateTime date, String name);

    List<Booking> findBookingsByDateAfterAndEmail(LocalDateTime date, String email);

    @Modifying
    @Transactional
    @Query(value = "UPDATE reservations SET status = ?2 WHERE id = ?1",
            nativeQuery = true)
    void updateStatus(int id, String status);
}
