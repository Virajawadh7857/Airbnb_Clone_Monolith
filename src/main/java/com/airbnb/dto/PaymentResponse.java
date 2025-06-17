package com.airbnb.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PaymentResponse {
    private Long id;
    private Long bookingId;
    private double amount;
    private String transactionId;
    private LocalDateTime paymentDate;
    private String status;
}
