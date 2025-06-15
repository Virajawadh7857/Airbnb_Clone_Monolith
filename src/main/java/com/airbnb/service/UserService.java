package com.airbnb.service;

import com.airbnb.dto.AuthResponse;

import com.airbnb.dto.LoginRequest;
import com.airbnb.entity.User;


public interface UserService {

	User registerUser(User user);
	
	//JWT-Based Login
	AuthResponse loginUser(LoginRequest request);
	
	

}
