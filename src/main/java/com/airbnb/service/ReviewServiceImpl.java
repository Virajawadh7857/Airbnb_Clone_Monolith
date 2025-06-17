package com.airbnb.service;

import com.airbnb.dto.ReviewRequest;
import com.airbnb.dto.ReviewResponse;
import com.airbnb.entity.Property;
import com.airbnb.entity.Review;
import com.airbnb.entity.User;
import com.airbnb.repository.PropertyRepository;
import com.airbnb.repository.ReviewRepository;
import com.airbnb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Override
    public ReviewResponse leaveReview(ReviewRequest request, String guestEmail) {
        User guest = userRepository.findByEmail(guestEmail)
                .orElseThrow(() -> new RuntimeException("Guest not found"));

        Property property = propertyRepository.findById(request.getPropertyId())
                .orElseThrow(() -> new RuntimeException("Property not found"));

        // ✅ Check if the guest already reviewed this property
        if (reviewRepository.existsByGuestAndProperty(guest, property)) {
            throw new RuntimeException("You have already reviewed this property.");
        }

        Review review = Review.builder()
                .guest(guest)
                .property(property)
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        Review saved = reviewRepository.save(review);
        return mapToResponse(saved);
    }

    @Override
    public List<ReviewResponse> getPropertyReviews(Long propertyId) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        // ✅ Updated to sort reviews by latest first (createdAt DESC)
        return reviewRepository.findByPropertyOrderByCreatedAtDesc(property)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private ReviewResponse mapToResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .guestEmail(review.getGuest().getEmail())
                .propertyId(review.getProperty().getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .build();
    }
}
