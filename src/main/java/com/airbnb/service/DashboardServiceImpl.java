package com.airbnb.service;

import com.airbnb.dto.AdminDashboardResponse;
import com.airbnb.dto.DashboardStatsResponse;
import com.airbnb.dto.HostDashboardResponse;
import com.airbnb.repository.UserRepository;
import com.airbnb.service.BookingService;
import com.airbnb.service.DashboardService;
import com.airbnb.service.PropertyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private BookingService bookingService;

    @Override
    public AdminDashboardResponse getAdminDashboardData() {
        long users = userRepository.count();
        long properties = propertyService.countAll();
        long bookings = bookingService.countAll();
        return new AdminDashboardResponse(users, properties, bookings);
    }

    @Override
    public DashboardStatsResponse getAdminDashboardStats() {
        long users = userRepository.count();
        long properties = propertyService.countAll();
        long bookings = bookingService.countAll();
        return new DashboardStatsResponse(users, properties, bookings);
    }

    @Override
    public HostDashboardResponse getHostDashboardData(String email) {
        long properties = propertyService.countHostProperties(email);
        long bookings = bookingService.countHostBookings(email);
        return new HostDashboardResponse(properties, bookings);
    }
}
