package com.airbnb.repository;

import com.airbnb.entity.Property;
import com.airbnb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Long> {
    List<Property> findByHost(User host);
}
