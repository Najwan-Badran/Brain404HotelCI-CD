package com.Review;

import com.Booking.BookingRepository;
import com.Booking.BookingStatus;
import com.Room.Room;
import com.Room.RoomRepository;
import com.User.User;
import com.User.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final ReviewMapper mapper;

    public ReviewServiceImpl(ReviewRepository reviewRepository,
                             UserRepository userRepository,
                             RoomRepository roomRepository,
                             BookingRepository bookingRepository,
                             ReviewMapper mapper) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
        this.mapper = mapper;
    }

    @Override
    public ReviewResponseDTO create(ReviewRequestDTO dto) {

        validateRating(dto.getRating());

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        validateCompletedBooking(dto.getUserId(), dto.getRoomId());

        preventDuplicateReview(dto.getUserId(), dto.getRoomId());

        Review review = new Review();
        review.setUser(user);
        review.setRoom(room);
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());

        Review saved = reviewRepository.save(review);

        updateRoomStatistics(room);

        return mapper.toDTO(saved);
    }

    @Override
    public List<ReviewResponseDTO> getByRoom(Long roomId) {

        if (!roomRepository.existsById(roomId)) {
            throw new IllegalArgumentException("Room not found");
        }

        return reviewRepository.findByRoom_IdOrderByCreatedAtDesc(roomId)
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public List<ReviewResponseDTO> getByHotel(Long hotelId) {
        // Optional enhancement later
        return reviewRepository.findByHotel_IdOrderByCreatedAtDesc(hotelId)
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    // ---------------- PRIVATE METHODS ----------------

    private void validateRating(Integer rating) {
        if (rating == null || rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
    }

    private void validateCompletedBooking(Long userId, Long roomId) {

        boolean hasCompletedBooking =
                bookingRepository.existsByUser_IdAndRoom_IdAndStatusAndCheckOutDateBefore(
                        userId,
                        roomId,
                        BookingStatus.CONFIRMED,
                        LocalDate.now()
                );

        if (!hasCompletedBooking) {
            throw new IllegalStateException(
                    "User must complete booking before reviewing this room"
            );
        }
    }

    private void preventDuplicateReview(Long userId, Long roomId) {

        if (reviewRepository.existsByUser_IdAndRoom_Id(userId, roomId)) {
            throw new IllegalStateException("User already reviewed this room");
        }
    }

    private void updateRoomStatistics(Room room) {

        Double avg = reviewRepository.calculateAverageForRoom(room.getId());
        Long count = reviewRepository.countByRoom_Id(room.getId());

        room.setAverageRating(avg != null ? avg : 0.0);
        room.setReviewCount(count == null ? 0 : count.intValue());    }

    @Override
    public ReviewSummaryDTO getRoomSummary(Long roomId) {

        if (!roomRepository.existsById(roomId)) {
            throw new IllegalArgumentException("Room not found");
        }

        Double avg = reviewRepository.calculateAverageForRoom(roomId);
        Long count = reviewRepository.countByRoom_Id(roomId);

        return new ReviewSummaryDTO(
                roomId,
                avg != null ? avg : 0.0,
                count != null ? count : 0
        );
    }
}
