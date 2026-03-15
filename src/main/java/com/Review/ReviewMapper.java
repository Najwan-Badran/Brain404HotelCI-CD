package com.Review;

import org.springframework.stereotype.Component;
@Component
public class ReviewMapper {

    public ReviewResponseDTO toDTO(Review review) {
        return new ReviewResponseDTO(
                review.getId(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt(),
                review.getUser().getId(),
                review.getRoom() != null ? review.getRoom().getId() : null,
                review.getHotel() != null ? review.getHotel().getId() : null
        );
    }
}