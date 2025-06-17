package com.airbnb.repository;

import com.airbnb.entity.Property;
import com.airbnb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Long> {

    List<Property> findByHost(User host);

    long countByHost(User host);

    // Basic filter (no date)
    @Query("""
        SELECT p FROM Property p
        WHERE (:location IS NULL OR LOWER(p.location) LIKE LOWER(CONCAT('%', :location, '%')))
          AND (:minPrice IS NULL OR p.pricePerNight >= :minPrice)
          AND (:maxPrice IS NULL OR p.pricePerNight <= :maxPrice)
          AND (:minGuests IS NULL OR p.maxGuests >= :minGuests)
    """)
    List<Property> searchProperties(
        @Param("location") String location,
        @Param("minPrice") Integer minPrice,
        @Param("maxPrice") Integer maxPrice,
        @Param("minGuests") Integer minGuests
    );

    // Filter with date range (exclude booked properties)
    @Query("""
        SELECT p FROM Property p
        WHERE (:location IS NULL OR LOWER(p.location) LIKE LOWER(CONCAT('%', :location, '%')))
          AND (:minPrice IS NULL OR p.pricePerNight >= :minPrice)
          AND (:maxPrice IS NULL OR p.pricePerNight <= :maxPrice)
          AND (:minGuests IS NULL OR p.maxGuests >= :minGuests)
          AND p.id NOT IN (
              SELECT b.property.id FROM Booking b
              WHERE b.status <> 'CANCELLED'
                AND b.startDate <= :endDate AND b.endDate >= :startDate
          )
    """)
    List<Property> searchAvailableProperties(
        @Param("location") String location,
        @Param("minPrice") Integer minPrice,
        @Param("maxPrice") Integer maxPrice,
        @Param("minGuests") Integer minGuests,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
}
