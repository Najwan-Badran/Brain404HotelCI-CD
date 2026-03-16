package com.Payment;

import com.Booking.Booking;
import com.Booking.BookingRepository;
import com.Booking.BookingStatus;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              BookingRepository bookingRepository) {
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public PaymentResponseDTO create(PaymentRequestDTO dto) {

        Booking booking = bookingRepository.findById(dto.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Validate amount
        if (dto.getAmount() == null || dto.getAmount().doubleValue() <= 0) {
            throw new RuntimeException("Payment amount must be greater than zero");
        }

        // Prevent duplicate payment for same booking
        if (paymentRepository.findByBooking_Id(booking.getId()).isPresent()) {
            throw new RuntimeException("Payment already exists for this booking");
        }

        // Set booking status to PENDING when payment created
        booking.setStatus(BookingStatus.PENDING);

        Payment payment = new Payment();
        payment.setAmount(dto.getAmount());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setCreatedAt(LocalDateTime.now());
        payment.setBooking(booking);

        Payment saved = paymentRepository.save(payment);

        return PaymentMapper.toDTO(saved);
    }

    @Override
    public PaymentResponseDTO markAsPaid(Long paymentId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (payment.getStatus() == PaymentStatus.PAID) {
            throw new RuntimeException("Payment already paid");
        }

        if (payment.getStatus() == PaymentStatus.CANCELLED) {
            throw new RuntimeException("Cancelled payment cannot be paid");
        }

        payment.setStatus(PaymentStatus.PAID);

        // Update booking status automatically
        Booking booking = payment.getBooking();
        booking.setStatus(BookingStatus.CONFIRMED);

        return PaymentMapper.toDTO(payment);
    }

    @Override
    public PaymentResponseDTO cancel(Long paymentId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (payment.getStatus() == PaymentStatus.PAID) {
            throw new RuntimeException("Paid payment cannot be cancelled");
        }

        payment.setStatus(PaymentStatus.CANCELLED);

        // Update booking status
        Booking booking = payment.getBooking();
        booking.setStatus(BookingStatus.CANCELLED);

        return PaymentMapper.toDTO(payment);
    }

    @Override
    public PaymentResponseDTO findByBooking(Long bookingId) {

        Payment payment = paymentRepository.findByBooking_Id(bookingId)
                .orElseThrow(() -> new RuntimeException("Payment not found for this booking"));

        return PaymentMapper.toDTO(payment);
    }
}