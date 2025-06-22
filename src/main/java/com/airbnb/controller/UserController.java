package com.airbnb.controller;

import com.airbnb.dto.AuthResponse;
import com.airbnb.dto.LoginRequest;
import com.airbnb.dto.PropertyResponse;
import com.airbnb.dto.UserDTO;
import com.airbnb.entity.User;
import com.airbnb.repository.UserRepository;
import com.airbnb.service.UserService;
import com.airbnb.service.UserServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired private UserRepository userRepository;
    


    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.registerUser(user));
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getCurrentUserProfile(Authentication authentication) {
        String email = authentication.getName(); // JWT-based
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Using your private method from UserServiceImpl
        UserDTO dto = ((UserServiceImpl) userService).mapToUserDTO(user);
        return ResponseEntity.ok(dto);
    }
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.loginUser(loginRequest));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<String> testAdminAccess() {
        return ResponseEntity.ok("You are an admin.");
    }

    @PreAuthorize("hasRole('HOST')")
    @GetMapping("/host")
    public ResponseEntity<String> testHostAccess() {
        return ResponseEntity.ok("You are a host.");
    }

    @PreAuthorize("hasRole('GUEST')")
    @GetMapping("/guest")
    public ResponseEntity<String> testGuestAccess() {
        return ResponseEntity.ok("You are a guest.");
    }

    // Favorites
    @PreAuthorize("hasRole('GUEST')")
    @PostMapping("/favorites/{propertyId}")
    public ResponseEntity<String> addFavorite(@PathVariable Long propertyId, Authentication authentication) {
        String email = authentication.getName();
        userService.addToFavorites(email, propertyId);
        return ResponseEntity.ok("Added to favorites.");
    }

    @PreAuthorize("hasRole('GUEST')")
    @DeleteMapping("/favorites/{propertyId}")
    public ResponseEntity<String> removeFavorite(@PathVariable Long propertyId, Authentication authentication) {
        String email = authentication.getName();
        userService.removeFromFavorites(email, propertyId);
        return ResponseEntity.ok("Removed from favorites.");
    }

    @PreAuthorize("hasRole('GUEST')")
    @GetMapping("/favorites")
    public ResponseEntity<List<PropertyResponse>> getFavorites(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(userService.getFavoriteProperties(email));
    }
}
