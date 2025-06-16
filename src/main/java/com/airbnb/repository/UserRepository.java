package com.airbnb.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.airbnb.entity.Role;
import com.airbnb.entity.User;


public interface UserRepository extends JpaRepository<User ,Long> {
	
	Optional<User> findByEmail(String email);
	long countByRole(Role role);


}
