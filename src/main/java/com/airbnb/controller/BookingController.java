package com.airbnb.controller;

import com.airbnb.dto.BookingRequest;
import com.airbnb.dto.BookingResponse;
import com.airbnb.service.BookingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PreAuthorize("hasRole('GUEST')")
    @PostMapping
    public BookingResponse createBooking(@RequestBody BookingRequest request, Authentication authentication) {
        String email = authentication.getName();
        return bookingService.createBooking(request, email);
    }

    @PreAuthorize("hasRole('GUEST')")
    @GetMapping
    public List<BookingResponse> getMyBookings(Authentication authentication) {
        String email = authentication.getName();
        return bookingService.getGuestBookings(email);
    }
    
    @PreAuthorize("hasRole('HOST')")
    @GetMapping("/host")
    public List<BookingResponse> getHostBookings(Authentication authentication) {
        String email = authentication.getName();
        return bookingService.getHostBookings(email);
    }


    @PreAuthorize("hasRole('GUEST')")
    @DeleteMapping("/{id}")
    public String cancelBooking(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        bookingService.cancelBooking(id, email);
        return "Booking cancelled successfully";
    }

    // Optional: Admin view all bookings
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/all")
    public List<BookingResponse> getAllBookings() {
        return bookingService.getAllBookings();
    }
}
