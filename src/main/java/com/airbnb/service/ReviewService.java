package com.airbnb.service;

import com.airbnb.dto.ReviewRequest;
import com.airbnb.dto.ReviewResponse;

import java.util.List;

public interface ReviewService {
    ReviewResponse leaveReview(ReviewRequest request, String guestEmail);
    List<ReviewResponse> getPropertyReviews(Long propertyId);
}
