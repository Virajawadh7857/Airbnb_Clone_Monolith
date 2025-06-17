package com.airbnb.controller;

import com.airbnb.dto.PaymentResponse;
import com.airbnb.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@PreAuthorize("hasRole('GUEST')")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // Make payment for booking
    
    @PostMapping("/pay/{bookingId}")
    public PaymentResponse makePayment(@PathVariable Long bookingId, Authentication authentication) {
        String email = authentication.getName();
        return paymentService.makePayment(bookingId, email);
    }

    // Refund Payment 
    
    @PreAuthorize("hasRole('GUEST')")
    @PostMapping("/refund/{transactionId}")
    public PaymentResponse refundPayment(@PathVariable String  transactionId, Authentication authentication) {
        String guestEmail = authentication.getName();
        return paymentService.refundPayment(transactionId, guestEmail);
    }

}
