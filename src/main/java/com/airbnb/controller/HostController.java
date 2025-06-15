package com.airbnb.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/host")
public class HostController {

    @PreAuthorize("hasRole('HOST')")
    @GetMapping("/dashboard")
    public ResponseEntity<String> hostDashboard() {
        return ResponseEntity.ok("Welcome, Host!");
    }
}
