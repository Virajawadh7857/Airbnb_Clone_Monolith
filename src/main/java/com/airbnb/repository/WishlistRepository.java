package com.airbnb.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.airbnb.entity.Property;
import com.airbnb.entity.User;
import com.airbnb.entity.Wishlist;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findByGuest(User guest);
    Optional<Wishlist> findByGuestAndProperty(User guest, Property property);
}

