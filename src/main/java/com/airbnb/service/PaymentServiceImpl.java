package com.airbnb.service;

import com.airbnb.client.NotificationClient;
import com.airbnb.dto.NotificationRequest;
import com.airbnb.dto.PaymentRequest;
import com.airbnb.dto.PaymentResponse;
import com.airbnb.entity.*;
import com.airbnb.repository.BookingRepository;
import com.airbnb.repository.PaymentRepository;
import com.airbnb.repository.UserRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationClient notificationClient;

    // ✅ MAIN METHOD: PROCESS PAYMENT
    @Override
    public PaymentResponse processPayment(PaymentRequest request) {
        // Step 1: Fetch booking
        Booking booking = bookingRepository.findById(request.getBookingId())
            .orElseThrow(() -> new RuntimeException("Booking not found"));

        User guest = booking.getGuest();
        Property property = booking.getProperty();
        LocalDate start = booking.getStartDate();
        LocalDate end = booking.getEndDate();

        // Step 2: Authorization check
        if (!guest.getEmail().equals(getAuthenticatedUserEmail())) {
            throw new RuntimeException("Unauthorized to pay for this booking.");
        }

        // Step 3: Calculate payment amount
        long days = ChronoUnit.DAYS.between(start, end);
        double amount = days * property.getPricePerNight();

        // Step 4: Simulate payment (with 20% random failure chance)
        boolean simulateFailure = Math.random() < 0.2;

        Payment payment = Payment.builder()
                .booking(booking)
                .amount(amount)
                .transactionId(UUID.randomUUID().toString())
                .paymentDate(LocalDateTime.now())
                .status(simulateFailure ? PaymentStatus.FAILED : PaymentStatus.PAID)
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        // Step 5: If payment is successful, confirm booking and notify user
        if (!simulateFailure) {
            booking.setStatus(BookingStatus.CONFIRMED);
            bookingRepository.save(booking);

            sendNotification(guest, property, amount, booking.getId(), start, end);
        }

        return mapToResponse(savedPayment);
    }

    // ✅ SEND EMAIL + SMS NOTIFICATION
    private void sendNotification(User guest, Property property, double amount, Long bookingId, LocalDate start, LocalDate end) {
        NotificationRequest notification = new NotificationRequest();

        // Email
        notification.setToEmail(guest.getEmail());
        notification.setSubject("Payment Confirmation");
        notification.setBody("Dear " + guest.getName()
                + ",\n\nYour payment of ₹" + amount
                + " for the property \"" + property.getTitle()
                + "\" (from " + start + " to " + end + ") was successful.\n\nThank you!");

        // SMS
        notification.setToPhoneNumber("+91" + guest.getPhoneNumber());
        notification.setSmsMessage("Hi " + guest.getName()
                + ", your payment of ₹" + amount
                + " for \"" + property.getTitle()
                + "\" was successful. Booking ID: " + bookingId);

        notificationClient.sendNotification(notification);
    }

    // ✅ REFUND PAYMENT
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

    // ✅ HELPER: Get logged-in user's email from Spring Security context
    private String getAuthenticatedUserEmail() {
        return org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication().getName();
    }

    // ✅ HELPER: Map Payment to Response DTO
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

    @Override
    public PaymentResponse makePayment(Long bookingId, String guestEmail) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getGuest().getEmail().equals(guestEmail)) {
            throw new RuntimeException("Unauthorized to pay for this booking.");
        }

        Property property = booking.getProperty();
        LocalDate start = booking.getStartDate();
        LocalDate end = booking.getEndDate();

        long days = ChronoUnit.DAYS.between(start, end);
        double amount = days * property.getPricePerNight();

        boolean simulateFailure = Math.random() < 0.2;

        Payment payment = Payment.builder()
                .booking(booking)
                .amount(amount)
                .transactionId(UUID.randomUUID().toString())
                .paymentDate(LocalDateTime.now())
                .status(simulateFailure ? PaymentStatus.FAILED : PaymentStatus.PAID)
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        if (!simulateFailure) {
            booking.setStatus(BookingStatus.CONFIRMED);
            bookingRepository.save(booking);

            NotificationRequest notification = new NotificationRequest();
            notification.setToEmail(guestEmail);
            notification.setSubject("Payment Confirmation");
            notification.setBody("Dear " + booking.getGuest().getName()
                    + ",\n\nYour payment of ₹" + amount
                    + " for \"" + property.getTitle()
                    + "\" from " + start + " to " + end + " was successful.");

            notification.setToPhoneNumber(  booking.getGuest().getPhoneNumber());
            notification.setSmsMessage("Hi " + booking.getGuest().getName()
                    + ", your payment of ₹" + amount
                    + " for \"" + property.getTitle()
                    + "\" was successful. Booking ID: " + booking.getId());

            notificationClient.sendNotification(notification);
        }

        return mapToResponse(savedPayment);
    }

}
