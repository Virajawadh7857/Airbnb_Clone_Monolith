package com.airbnb.service;

import com.airbnb.dto.PaymentResponse;
import com.airbnb.entity.Booking;
import com.airbnb.entity.BookingStatus;
import com.airbnb.entity.Payment;
import com.airbnb.entity.PaymentStatus;
import com.airbnb.entity.User;
import com.airbnb.repository.BookingRepository;
import com.airbnb.repository.PaymentRepository;
import com.airbnb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    // ✅ Simulated Payment
    @Override
    public PaymentResponse makePayment(Long bookingId, String guestEmail) {
        User guest = userRepository.findByEmail(guestEmail)
                .orElseThrow(() -> new RuntimeException("Guest not found"));

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getGuest().getEmail().equals(guestEmail)) {
            throw new RuntimeException("Unauthorized to pay for this booking.");
        }

        // ✅ Simulate failure
        boolean simulateFailure = Math.random() < 0.2;

        double amount = calculateAmount(booking); // assume flat rate

        Payment payment = Payment.builder()
                .booking(booking)
                .amount(amount)
                .transactionId(UUID.randomUUID().toString())
                .paymentDate(LocalDateTime.now())
                .status(simulateFailure ? PaymentStatus.FAILED : PaymentStatus.PAID)
                .build();

        Payment saved = paymentRepository.save(payment);

        if (!simulateFailure) {
            booking.setStatus(BookingStatus.CONFIRMED);
            bookingRepository.save(booking);
        }

        return mapToResponse(saved);
    }

    // ✅ Refund Payment
    @Override
    public PaymentResponse refundPayment(String transactionId, String guestEmail) {
        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (!payment.getBooking().getGuest().getEmail().equals(guestEmail)) {
            throw new RuntimeException("Unauthorized to refund this payment.");
        }

        if (payment.getStatus() != PaymentStatus.PAID) {
            throw new RuntimeException("Cannot refund a non-paid transaction.");
        }

        payment.setStatus(PaymentStatus.REFUNDED);
        paymentRepository.save(payment);

        Booking booking = payment.getBooking();
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        return mapToResponse(payment);
    }

    // ✅ DTO Mapper
    private PaymentResponse mapToResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .bookingId(payment.getBooking().getId())
                .amount(payment.getAmount())
                .transactionId(payment.getTransactionId())
                .paymentDate(payment.getPaymentDate())
                .status(payment.getStatus().toString())
                .build();
    }

    // ✅ Dummy rate calculator
    private double calculateAmount(Booking booking) {
        return 100.0; // static for now
    }
}
