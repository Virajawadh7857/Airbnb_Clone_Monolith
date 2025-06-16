package com.airbnb.controller;

import com.airbnb.dto.BookingRequest;
import com.airbnb.dto.BookingResponse;
import com.airbnb.service.BookingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    // ✅ Create a booking (GUEST only)
    @PostMapping
    @PreAuthorize("hasRole('GUEST')")
    public ResponseEntity<BookingResponse> createBooking(
            @RequestBody BookingRequest request,
            Authentication authentication) {
        String email = authentication.getName();
        BookingResponse response = bookingService.createBooking(request, email);
        return ResponseEntity.ok(response);
    }

    // ✅ View guest's bookings (GUEST only)
    @GetMapping
    @PreAuthorize("hasRole('GUEST')")
    public ResponseEntity<List<BookingResponse>> getMyBookings(Authentication authentication) {
        String email = authentication.getName();
        List<BookingResponse> bookings = bookingService.getGuestBookings(email);
        return ResponseEntity.ok(bookings);
    }

    // ✅ View host's bookings (HOST only)
    @GetMapping("/host")
    @PreAuthorize("hasRole('HOST')")
    public ResponseEntity<List<BookingResponse>> getHostBookings(Authentication authentication) {
        String email = authentication.getName();
        List<BookingResponse> bookings = bookingService.getHostBookings(email);
        return ResponseEntity.ok(bookings);
    }

    // ✅ Cancel booking (GUEST only)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('GUEST')")
    public ResponseEntity<String> cancelBooking(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        bookingService.cancelBooking(id, email);
        return ResponseEntity.ok("Booking cancelled successfully.");
    }

    // ✅ Admin: view all bookings (ADMIN only)
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        List<BookingResponse> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }
}
