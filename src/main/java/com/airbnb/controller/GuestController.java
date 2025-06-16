package com.airbnb.controller;

import com.airbnb.dto.BookingRequest;
import com.airbnb.dto.BookingResponse;
import com.airbnb.dto.PropertyResponse;
import com.airbnb.dto.ReviewRequest;
import com.airbnb.dto.ReviewResponse;
import com.airbnb.service.BookingService;
import com.airbnb.service.PropertyService;
import com.airbnb.service.ReviewService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guest")
@PreAuthorize("hasRole('GUEST')")
public class GuestController {

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ReviewService reviewService;

    // Get all available properties
    @GetMapping("/properties")
    public List<PropertyResponse> getAllProperties() {
        return propertyService.getAllProperties();
    }

    // Get a specific property by ID
    @GetMapping("/properties/{id}")
    public PropertyResponse getPropertyById(@PathVariable Long id) {
        return propertyService.getPropertyById(id);
    }

    // Book a property
    @PostMapping("/bookings")
    public BookingResponse createBooking(@RequestBody BookingRequest request, Authentication authentication) {
        String email = authentication.getName();
        return bookingService.createBooking(request, email);
    }

    // View guest's bookings
    @GetMapping("/bookings")
    public List<BookingResponse> getMyBookings(Authentication authentication) {
        String email = authentication.getName();
        return bookingService.getGuestBookings(email);
    }

    // Cancel booking
    @DeleteMapping("/bookings/{id}")
    public String cancelBooking(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        bookingService.cancelBooking(id, email);
        return "Booking cancelled successfully";
    }

    // Leave a review
    @PostMapping("/reviews")
    public ReviewResponse leaveReview(@RequestBody ReviewRequest request, Authentication authentication) {
        String email = authentication.getName();
        return reviewService.leaveReview(request, email);
    }

    // Get reviews for a property
    @GetMapping("/properties/{propertyId}/reviews")
    public List<ReviewResponse> getPropertyReviews(@PathVariable Long propertyId) {
        return reviewService.getPropertyReviews(propertyId);
    }
}
