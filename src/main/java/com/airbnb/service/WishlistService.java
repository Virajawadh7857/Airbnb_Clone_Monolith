package com.airbnb.service;

import java.util.List;

import com.airbnb.dto.WishlistResponse;

public interface WishlistService {
    WishlistResponse addToWishlist(Long propertyId, String guestEmail);
    void removeFromWishlist(Long propertyId, String guestEmail);
    List<WishlistResponse> getGuestWishlist(String guestEmail);
}

