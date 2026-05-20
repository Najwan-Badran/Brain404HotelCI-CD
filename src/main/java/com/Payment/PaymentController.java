package com.Payment;

import com.PagedResponse;
import com.PaymentTransaction.PaymentTransactionResponseDTO;
import com.Security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    // ==========================================================
    // Global & Status Queries (Admin/Manager)
    // ==========================================================

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @Operation(summary = "Get all payments", description = "Retrieve a paginated list of all payments in the system")
    public PagedResponse<PaymentResponseDTO> getAllPayments(Pageable pageable) {
        Page<PaymentResponseDTO> page = service.getAll(pageable);
        return PagedResponse.from(page);
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @Operation(summary = "Get payments by status", description = "Retrieve a paginated list of payments filtered by status")
    public PagedResponse<PaymentResponseDTO> getByStatus(@PathVariable String status, Pageable pageable) {
        Page<PaymentResponseDTO> page = service.findByStatus(status, pageable);
        return PagedResponse.from(page);
    }

    // ==========================================================
    // User Specific Queries
    // ==========================================================

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','USER')")
    @Operation(summary = "Get my payments", description = "Retrieve a paginated list of payments for the authenticated user")
    public PagedResponse<PaymentResponseDTO> getMyPayments(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Pageable pageable) {
        Page<PaymentResponseDTO> page = service.findByUserId(userDetails.getId(), pageable);
        return PagedResponse.from(page);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public PagedResponse<PaymentResponseDTO> getByUserId(@PathVariable Long userId, Pageable pageable) {
        Page<PaymentResponseDTO> page = service.findByUserId(userId, pageable);
        return PagedResponse.from(page);
    }

    // ==========================================================
    // CRUD Operations
    // ==========================================================

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','USER')")
    public PaymentResponseDTO create(@Valid @RequestBody PaymentRequestDTO dto) {
        return service.create(dto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public PaymentResponseDTO getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}/pay")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public PaymentResponseDTO markAsPaid(@PathVariable Long id) {
        return service.markAsPaid(id);
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','USER')")
    public PaymentResponseDTO cancel(@PathVariable Long id) {
        return service.cancel(id);
    }

    @GetMapping("/booking/{bookingId}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public List<PaymentResponseDTO> getByBooking(@PathVariable Long bookingId) {
        return service.findByBooking(bookingId);
    }

    // ==========================================================
    // Refund Endpoints
    // ==========================================================

    @PostMapping("/{id}/refund")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public PaymentResponseDTO refund(@PathVariable Long id,
                                     @RequestParam(required = false) String reason) {
        return service.refund(id, reason);
    }

    // FIXED: Changed to @RequestParam to match Postman collection
    @PostMapping("/{id}/partial-refund")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public PaymentResponseDTO partialRefund(@PathVariable Long id,
                                            @RequestParam BigDecimal amount,
                                            @RequestParam(required = false) String reason) {
        return service.partialRefund(id, amount, reason);
    }

    // ==========================================================
    // Transaction History & Summaries
    // ==========================================================

    @GetMapping("/{id}/transactions")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public List<PaymentTransactionResponseDTO> getPaymentTransactions(@PathVariable Long id) {
        return service.getPaymentTransactions(id);
    }

    @GetMapping("/booking/{bookingId}/summary")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public PaymentSummaryDTO getBookingPaymentSummary(@PathVariable Long bookingId) {
        return service.getBookingPaymentSummary(bookingId);
    }
}