package com.airbnb.service;

import com.airbnb.dto.WishlistResponse;
import com.airbnb.entity.Property;
import com.airbnb.entity.User;
import com.airbnb.entity.Wishlist;
import com.airbnb.repository.PropertyRepository;
import com.airbnb.repository.UserRepository;
import com.airbnb.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishlistServiceImpl implements WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public WishlistResponse addToWishlist(Long propertyId, String guestEmail) {
        User guest = userRepository.findByEmail(guestEmail)
                .orElseThrow(() -> new RuntimeException("Guest not found"));

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        wishlistRepository.findByGuestAndProperty(guest, property).ifPresent(w -> {
            throw new RuntimeException("Property already in wishlist");
        });

        Wishlist wishlist = Wishlist.builder()
                .guest(guest)
                .property(property)
                .build();

        Wishlist saved = wishlistRepository.save(wishlist);
        return mapToResponse(saved);
    }

    @Override
    public void removeFromWishlist(Long propertyId, String guestEmail) {
        User guest = userRepository.findByEmail(guestEmail)
                .orElseThrow(() -> new RuntimeException("Guest not found"));

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        Wishlist wishlist = wishlistRepository.findByGuestAndProperty(guest, property)
                .orElseThrow(() -> new RuntimeException("Wishlist entry not found"));

        wishlistRepository.delete(wishlist);
    }

    @Override
    public List<WishlistResponse> getGuestWishlist(String guestEmail) {
        User guest = userRepository.findByEmail(guestEmail)
                .orElseThrow(() -> new RuntimeException("Guest not found"));

        return wishlistRepository.findByGuest(guest)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private WishlistResponse mapToResponse(Wishlist wishlist) {
        return WishlistResponse.builder()
                .id(wishlist.getId())
                .propertyId(wishlist.getProperty().getId())
                .propertyTitle(wishlist.getProperty().getTitle())
                .guestEmail(wishlist.getGuest().getEmail())
                .build();
    }
}
