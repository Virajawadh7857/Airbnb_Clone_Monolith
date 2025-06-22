package com.airbnb.service;

import java.util.List;

import com.airbnb.dto.AuthResponse;
import com.airbnb.dto.LoginRequest;
import com.airbnb.dto.PropertyResponse;
import com.airbnb.dto.UserDTO;
import com.airbnb.entity.User;

public interface UserService {

    User registerUser(User user);
    UserDTO mapToUserDTO(User user);

    
    // JWT-Based Login
    AuthResponse loginUser(LoginRequest request);
    
    void addToFavorites(String email, Long propertyId);
    void removeFromFavorites(String email, Long propertyId);
    List<PropertyResponse> getFavoriteProperties(String email);
}
