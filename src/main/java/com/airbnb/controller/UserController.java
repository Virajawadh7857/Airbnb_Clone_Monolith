package com.airbnb.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.airbnb.dto.LoginRequest;
import com.airbnb.entity.User;
import com.airbnb.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.registerUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.loginUser(loginRequest));
    }
    
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> testAdminAccess() {
        return ResponseEntity.ok("You are an admin.");
    }
    
    @GetMapping("/host")
    @PreAuthorize("hasRole('HOST')")
    public ResponseEntity<String> testHost() {
        return ResponseEntity.ok("You are a host.");
    }

    @GetMapping("/guest")
    @PreAuthorize("hasRole('GUEST')")
    public ResponseEntity<String> testGuest() {
        return ResponseEntity.ok("You are a guest.");
    }


}

