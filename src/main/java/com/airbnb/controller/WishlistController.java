package com.airbnb.controller;

import com.airbnb.dto.WishlistResponse;
import com.airbnb.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @PreAuthorize("hasRole('GUEST')")
    @PostMapping("/{propertyId}")
    public ResponseEntity<WishlistResponse> addToWishlist(
            @PathVariable Long propertyId, Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(wishlistService.addToWishlist(propertyId, email));
    }

    @PreAuthorize("hasRole('GUEST')")
    @DeleteMapping("/{propertyId}")
    public ResponseEntity<?> removeFromWishlist(
            @PathVariable Long propertyId, Authentication authentication) {
        String email = authentication.getName();
        wishlistService.removeFromWishlist(propertyId, email);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('GUEST')")
    @GetMapping
    public ResponseEntity<List<WishlistResponse>> getWishlist(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(wishlistService.getGuestWishlist(email));
    }
}
