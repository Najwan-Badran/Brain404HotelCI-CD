package com.Payment;


public interface PaymentService {

    PaymentResponseDTO create(PaymentRequestDTO dto);

    PaymentResponseDTO markAsPaid(Long paymentId);

    PaymentResponseDTO cancel(Long paymentId);

    PaymentResponseDTO findByBooking(Long bookingId);
}