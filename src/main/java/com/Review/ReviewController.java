package com.Review;

import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService service;

    public ReviewController(ReviewService service) {
        this.service = service;
    }

    @PostMapping
    public ReviewResponseDTO create(@Valid @RequestBody ReviewRequestDTO dto) {
        return service.create(dto);
    }

    @GetMapping("/room/{roomId}")
    public List<ReviewResponseDTO> getByRoom(@PathVariable Long roomId) {
        return service.getByRoom(roomId);
    }

    @GetMapping("/hotel/{hotelId}")
    public List<ReviewResponseDTO> getByHotel(@PathVariable Long hotelId) {
        return service.getByHotel(hotelId);
    }

    @GetMapping("/room/{roomId}/summary")
    public ReviewSummaryDTO getRoomSummary(@PathVariable Long roomId) {
        return service.getRoomSummary(roomId);
    }
}