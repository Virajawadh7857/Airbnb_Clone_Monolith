package com.airbnb.controller;

import com.airbnb.dto.DashboardStatsResponse;
import com.airbnb.dto.HostDashboardResponse;
import com.airbnb.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/host")
@PreAuthorize("hasRole('HOST')")
public class HostController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/dashboard")
    public ResponseEntity<HostDashboardResponse> getHostDashboard(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(dashboardService.getHostDashboardData(email));
    }

    @GetMapping("/test")
    public ResponseEntity<String> testHost() {
        return ResponseEntity.ok("You are a host.");
    }
}
