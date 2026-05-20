package com.Payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentResponseDTO {

    private Long id;
    private BigDecimal amount;
    private PaymentStatus status;
    private PaymentMethod paymentMethod;
    private LocalDateTime createdAt;
    private Long bookingId;

    public PaymentResponseDTO(Long id, BigDecimal amount,
                              PaymentStatus status,
                              PaymentMethod paymentMethod,
                              LocalDateTime createdAt,
                              Long bookingId) {
        this.id = id;
        this.amount = amount;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.createdAt = createdAt;
        this.bookingId = bookingId;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Long getBookingId() {
        return bookingId;
    }
}