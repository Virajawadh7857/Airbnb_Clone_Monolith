package com.airbnb.repository;

import com.airbnb.entity.Property;
import com.airbnb.entity.Review;
import com.airbnb.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
	
    List<Review> findByProperty(Property property); 
    boolean existsByGuestAndProperty(User guest, Property property);

    List<Review> findByPropertyOrderByCreatedAtDesc(Property property);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.property.id = :propertyId")
    Double getAverageRatingByPropertyId(@Param("propertyId") Long propertyId);

}

