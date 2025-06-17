package com.airbnb.dto;

import lombok.Data;

@Data
public class PaymentRequest {
    private Long bookingId;
    private String paymentMethod; // Optional: card, upi, etc.
}
