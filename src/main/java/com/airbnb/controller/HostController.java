package com.airbnb.controller;

import com.airbnb.dto.BookingResponse;
import com.airbnb.dto.HostDashboardResponse;
import com.airbnb.dto.HostDashboardStatsResponse;
import com.airbnb.dto.PropertyRequest;
import com.airbnb.dto.PropertyResponse;
import com.airbnb.service.BookingService;
import com.airbnb.service.PropertyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/host")
@PreAuthorize("hasRole('HOST')")
public class HostController {

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private BookingService bookingService;

    // ✅ Create new property
    @PostMapping("/properties")
    public PropertyResponse createProperty(@RequestBody PropertyRequest request, Authentication authentication) {
        String email = authentication.getName();
        return propertyService.createProperty(request, email);
    }

    // ✅ Get host's properties
    @GetMapping("/properties")
    public List<PropertyResponse> getHostProperties(Authentication authentication) {
        String email = authentication.getName();
        return propertyService.getHostProperties(email);
    }

    // ✅ Get a property by ID
    @GetMapping("/properties/{id}")
    public PropertyResponse getPropertyById(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        PropertyResponse response = propertyService.getPropertyById(id);

        if (!response.getHostEmail().equals(email)) {
            throw new RuntimeException("Unauthorized to view this property.");
        }

        return response;
    }

    // ✅ Update property
    @PutMapping("/properties/{id}")
    public PropertyResponse updateProperty(
            @PathVariable Long id,
            @RequestBody PropertyRequest request,
            Authentication authentication) {
        String email = authentication.getName();
        return propertyService.updateProperty(id, request, email);
    }

    // ✅ Delete property
    @DeleteMapping("/properties/{id}")
    public String deleteProperty(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        propertyService.deleteProperty(id, email);
        return "Property deleted successfully.";
    }

    // ✅ Get bookings for host
    @GetMapping("/bookings")
    public List<BookingResponse> getAllHostBookings(Authentication authentication) {
        String email = authentication.getName();
        return bookingService.getHostBookings(email);
    }

    // ✅ Dashboard stats
    @GetMapping("/dashboard-stats")
    public HostDashboardStatsResponse getHostDashboardStats(Authentication authentication) {
        String email = authentication.getName();
        long propertyCount = propertyService.countHostProperties(email);
        long bookingCount = bookingService.countHostBookings(email);
        return new HostDashboardStatsResponse(propertyCount, bookingCount);
    }

    // ✅ Host dashboard (alternative data)
    @GetMapping("/dashboard")
    public HostDashboardResponse getHostDashboard(Authentication authentication) {
        String email = authentication.getName();
        long properties = propertyService.countHostProperties(email);
        long bookings = bookingService.countHostBookings(email);
        return new HostDashboardResponse(properties, bookings);
    }
}
