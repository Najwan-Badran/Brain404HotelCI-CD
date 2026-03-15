package com.Booking;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingResponseDTO create(@Valid @RequestBody BookingRequestDTO dto) {
        return bookingService.create(dto);
    }

    @PutMapping("/{id}/cancel")
    public BookingResponseDTO cancel(@PathVariable Long id) {
        return bookingService.cancel(id);
    }

    @GetMapping
    public List<BookingResponseDTO> getAll() {
        return bookingService.getAll();
    }
}