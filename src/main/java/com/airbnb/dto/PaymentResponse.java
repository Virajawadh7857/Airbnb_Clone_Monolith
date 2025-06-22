package com.airbnb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
	
	public PaymentResponse(String string) {
	}
	private Long id;
    private Long bookingId;
    private double amount;
    private String transactionId;
    private LocalDateTime paymentDate;
    private String status;
    private String message; 
    
}
