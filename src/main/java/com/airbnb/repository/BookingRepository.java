package com.airbnb.repository;

import com.airbnb.entity.Booking;
import com.airbnb.entity.Property;
import com.airbnb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByGuest(User guest);
    List<Booking> findByProperty(Property property);
	long countByProperty(Property p);
	long countByProperty_Host(User host);
	boolean existsByPropertyAndStartDateLessThanEqualAndEndDateGreaterThanEqual(Property property, LocalDate end, LocalDate start);

	
}
