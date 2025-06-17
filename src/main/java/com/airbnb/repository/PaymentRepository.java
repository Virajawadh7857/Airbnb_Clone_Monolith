package com.airbnb.repository;

import com.airbnb.entity.Payment;
import com.airbnb.entity.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByBookingId(Long bookingId);

	Optional<Payment> findByTransactionId(String transactionId);
}
