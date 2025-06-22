package com.airbnb.dto;

public record UserDTO(
		Long id,
		String name,
		String email, String role,
		Long phoneNumber) {
}
