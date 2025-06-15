package com.airbnb.repository;

import com.airbnb.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByPropertyId(Long propertyId); // 🔧 THIS LINE FIXES THE ERROR
}
