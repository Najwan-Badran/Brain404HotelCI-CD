package com.Booking;

import com.Room.Room;
import com.Room.RoomRepository;
import com.User.User;
import com.User.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final BookingMapper mapper;

    public BookingServiceImpl(BookingRepository bookingRepository,
                              RoomRepository roomRepository,
                              UserRepository userRepository,
                              BookingMapper mapper) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    // CREATE BOOKING
    @Override
    public BookingResponseDTO create(BookingRequestDTO dto) {

        if (!dto.getCheckOutDate().isAfter(dto.getCheckInDate())) {
            throw new RuntimeException("Check-out must be after check-in");
        }

        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Prevent double booking
        List<Booking> overlapping = bookingRepository.findOverlappingBookings(
                room.getId(),
                dto.getCheckInDate(),
                dto.getCheckOutDate()
        );

        if (!overlapping.isEmpty()) {
            throw new RuntimeException("Room already booked for selected dates");
        }

        Booking booking = new Booking();
        booking.setRoom(room);
        booking.setUser(user);
        booking.setCheckInDate(dto.getCheckInDate());
        booking.setCheckOutDate(dto.getCheckOutDate());
        booking.setNumberOfGuests(dto.getNumberOfGuests());

        // Calculate total price
        double totalPrice = calculateTotalPrice(room,
                dto.getCheckInDate(),
                dto.getCheckOutDate());

        booking.setTotalPrice(totalPrice);
        booking.setStatus(BookingStatus.PENDING);

        Booking saved = bookingRepository.save(booking);

        return mapper.toDTO(saved);
    }

    // CANCEL WITH POLICY (48 hours rule)
    @Override
    public BookingResponseDTO cancel(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (LocalDate.now().plusDays(2).isAfter(booking.getCheckInDate())) {
            throw new RuntimeException("Cancellation not allowed within 48 hours of check-in");
        }

        booking.setStatus(BookingStatus.CANCELLED);

        return mapper.toDTO(booking);
    }

    //  GET ALL
    @Override
    public List<BookingResponseDTO> getAll() {
        return bookingRepository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    // USER HISTORY
    public List<BookingResponseDTO> getUserBookings(Long userId) {
        return bookingRepository.findByUserId(userId)
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    // UPCOMING BOOKINGS
    public List<BookingResponseDTO> getUpcomingBookings() {
        return bookingRepository.findByCheckInDateAfter(LocalDate.now())
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    // PRICING LOGIC (Weekend +20%)
    private double calculateTotalPrice(Room room,
                                       LocalDate checkIn,
                                       LocalDate checkOut) {

        long days = ChronoUnit.DAYS.between(checkIn, checkOut);
        double basePrice = room.getRoomType().getPricePerNight();
        double total = 0;

        for (int i = 0; i < days; i++) {
            LocalDate date = checkIn.plusDays(i);

            if (date.getDayOfWeek() == DayOfWeek.FRIDAY ||
                    date.getDayOfWeek() == DayOfWeek.SATURDAY) {

                total += basePrice * 1.2;
            } else {
                total += basePrice;
            }
        }

        return total;
    }
}