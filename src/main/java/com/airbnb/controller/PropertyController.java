package com.airbnb.controller;

import com.airbnb.dto.PropertyRequest;
import com.airbnb.dto.PropertyResponse;
import com.airbnb.service.PropertyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    // Host creates a property
    @PreAuthorize("hasRole('HOST')")
    @PostMapping
    public PropertyResponse createProperty(@RequestBody PropertyRequest request, Authentication authentication) {
        String email = authentication.getName();
        return propertyService.createProperty(request, email);
    }

    // Guest views all properties
    @PreAuthorize("hasRole('GUEST')")
    @GetMapping
    public List<PropertyResponse> getAllProperties() {
        return propertyService.getAllProperties();
    }

    // Host views only their own properties
    @PreAuthorize("hasRole('HOST')")
    @GetMapping("/host")
    public List<PropertyResponse> getHostProperties(Authentication authentication) {
        String email = authentication.getName();
        return propertyService.getHostProperties(email);
    }
    
 // GET property by ID
    @PreAuthorize("hasAnyRole('GUEST', 'HOST')")
    @GetMapping("/{id}")
    public PropertyResponse getPropertyById(@PathVariable Long id) {
        return propertyService.getPropertyById(id);
    }

    // PUT update property (host only)
    @PreAuthorize("hasRole('HOST')")
    @PutMapping("/{id}")
    public PropertyResponse updateProperty(@PathVariable Long id,
                                           @RequestBody PropertyRequest request,
                                           Authentication authentication) {
        String email = authentication.getName();
        return propertyService.updateProperty(id, request, email);
    }
    

    // DELETE property (host only)
    @PreAuthorize("hasRole('HOST')")
    @DeleteMapping("/{id}")
    public String deleteProperty(@PathVariable Long id,
                                 Authentication authentication) {
        String email = authentication.getName();
        propertyService.deleteProperty(id, email);
        return "Property deleted successfully";
    }

}
