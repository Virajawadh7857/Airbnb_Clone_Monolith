package com.airbnb.controller;

import com.airbnb.dto.ReviewRequest;
import com.airbnb.dto.ReviewResponse;
import com.airbnb.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PreAuthorize("hasRole('GUEST')")
    @PostMapping
    public ReviewResponse leaveReview(@RequestBody ReviewRequest request, Authentication authentication) {
        String email = authentication.getName();
        return reviewService.leaveReview(request, email);
    }

    @GetMapping("/property/{propertyId}")
    public List<ReviewResponse> getPropertyReviews(@PathVariable Long propertyId) {
        return reviewService.getPropertyReviews(propertyId);
    }
}
