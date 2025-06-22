package com.airbnb.service;

import com.airbnb.dto.PaymentRequest;
import com.airbnb.dto.PaymentResponse;

public interface PaymentService {

    PaymentResponse makePayment(Long bookingId, String guestEmail);

    PaymentResponse refundPayment(String transactionId, String guestEmail);

	PaymentResponse processPayment(PaymentRequest request);
}
