package com.Booking;

import org.springframework.stereotype.Component;

@Component
public class BookingMapper {

    public BookingResponseDTO toDTO(Booking booking) {

        BookingResponseDTO dto = new BookingResponseDTO();

        dto.setId(booking.getId());
        dto.setRoomId(booking.getRoom().getId());
        dto.setUserId(booking.getUser().getId());
        dto.setCheckInDate(booking.getCheckInDate());
        dto.setCheckOutDate(booking.getCheckOutDate());
        dto.setNumberOfGuests(booking.getNumberOfGuests());
        dto.setStatus(booking.getStatus());
        dto.setTotalPrice(booking.getTotalPrice());
        dto.setCreatedAt(booking.getCreatedAt());

        return dto;
    }
}