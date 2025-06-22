package com.airbnb.service;

import com.airbnb.dto.AuthResponse;
import com.airbnb.dto.LoginRequest;
import com.airbnb.dto.PropertyResponse;
import com.airbnb.dto.UserDTO;
import com.airbnb.entity.Property;
import com.airbnb.entity.Review;
import com.airbnb.entity.Role;
import com.airbnb.entity.User;
import com.airbnb.repository.PropertyRepository;
import com.airbnb.repository.ReviewRepository;
import com.airbnb.repository.UserRepository;
import com.airbnb.security.JwtService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtService jwtService;
    @Autowired private PropertyRepository propertyRepository;
    @Autowired private ReviewRepository reviewRepository;

    @Override
    public User registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists..!");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getRole() == null) {
            user.setRole(Role.GUEST);  // default role
        }

        if (user.getPhoneNumber() == null || user.getPhoneNumber().isBlank()) {
            throw new RuntimeException("Phone number is required for registration.");
        }

        user.setFavorites(new ArrayList<>()); // initialize favorites list
        return userRepository.save(user);
    }

    @Override
    public AuthResponse loginUser(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );

        String token = jwtService.generateToken(userDetails);
        return new AuthResponse(token, "Login successful");
    }

    @Override
    public void addToFavorites(String email, Long propertyId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        if (!user.getFavorites().contains(property)) {
            user.getFavorites().add(property);
            userRepository.save(user);
        }
    }

    @Override
    public void removeFromFavorites(String email, Long propertyId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        user.getFavorites().remove(property);
        userRepository.save(user);
    }

    @Override
    public List<PropertyResponse> getFavoriteProperties(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getFavorites().stream()
                .map(this::mapToPropertyResponse)
                .collect(Collectors.toList());
    }

    private PropertyResponse mapToPropertyResponse(Property property) {
        List<Review> reviews = reviewRepository.findByProperty(property);
        double avgRating = reviews.isEmpty() ? 0.0 :
                reviews.stream().mapToDouble(Review::getRating).average().orElse(0.0);

        return PropertyResponse.builder()
                .id(property.getId())
                .title(property.getTitle())
                .description(property.getDescription())
                .location(property.getLocation())
                .pricePerNight(property.getPricePerNight())
                .maxGuests(property.getMaxGuests())
                .imageUrl(property.getImageUrl())
                .hostEmail(property.getHost().getEmail())
                .averageRating(avgRating)
                .build();
    }

	@Override
	public UserDTO mapToUserDTO(User user) {
		// TODO Auto-generated method stub
		return null;
	}
    
  

}
