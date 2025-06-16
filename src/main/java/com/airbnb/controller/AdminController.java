package com.airbnb.controller;

import com.airbnb.dto.AdminDashboardResponse;
import com.airbnb.dto.BookingResponse;
import com.airbnb.dto.PropertyResponse;
import com.airbnb.entity.User;
import com.airbnb.repository.UserRepository;
import com.airbnb.service.BookingService;
import com.airbnb.service.DashboardService;
import com.airbnb.service.PropertyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private BookingService bookingService;
    
    @Autowired
    private DashboardService dashboardService;


    //  View all users
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    //  View all properties
    @GetMapping("/properties")
    public List<PropertyResponse> getAllProperties() {
        return propertyService.getAllProperties();
    }

    //  View all bookings
    @GetMapping("/bookings")
    public List<BookingResponse> getAllBookings() {
        return bookingService.getAllBookings();
    }

    //  Delete a user
    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
        return "User deleted successfully";
    }

    //  Admin dashboard
    @GetMapping("/dashboard")
    public AdminDashboardResponse getDashboardStats() {
        return AdminDashboardResponse.builder()
                .totalUsers(dashboardService.countAllUsers())
                .totalGuests(dashboardService.countGuests())
                .totalHosts(dashboardService.countHosts())
                .totalAdmins(dashboardService.countAdmins())
                .totalProperties(dashboardService.countAllProperties())
                .totalBookings(dashboardService.countAllBookings())
                .build();
    }
}
