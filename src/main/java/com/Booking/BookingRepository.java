package com.Booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    boolean existsByUser_IdAndRoom_IdAndStatusAndCheckOutDateBefore(
            Long userId,
            Long roomId,
            BookingStatus status,
            LocalDate date
    );
    //  Double booking check
    @Query("""
        SELECT b FROM Booking b
        WHERE b.room.id = :roomId
        AND b.status <> 'CANCELLED'
        AND b.checkInDate < :checkOut
        AND b.checkOutDate > :checkIn
    """)
    List<Booking> findOverlappingBookings(Long roomId,
                                          LocalDate checkIn,
                                          LocalDate checkOut);

    // User booking history
    List<Booking> findByUserId(Long userId);


    // Upcoming bookings
    List<Booking> findByCheckInDateAfter(LocalDate today);
}