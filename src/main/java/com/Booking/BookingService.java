package com.Booking;

import java.util.List;

public interface BookingService {

    BookingResponseDTO create(BookingRequestDTO dto);

    BookingResponseDTO cancel(Long bookingId);

    List<BookingResponseDTO> getAll();
}