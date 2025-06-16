package com.airbnb.controller;

import com.airbnb.dto.PropertyRequest;
import com.airbnb.dto.PropertyResponse;
import com.airbnb.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @PreAuthorize("hasRole('HOST')")
    @PostMapping
    public ResponseEntity<PropertyResponse> createProperty(@RequestBody PropertyRequest request, Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(propertyService.createProperty(request, email));
    }

    @PreAuthorize("hasRole('GUEST')")
    @GetMapping
    public ResponseEntity<List<PropertyResponse>> getAllProperties() {
        return ResponseEntity.ok(propertyService.getAllProperties());
    }

    @PreAuthorize("hasRole('HOST')")
    @GetMapping("/host")
    public ResponseEntity<List<PropertyResponse>> getHostProperties(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(propertyService.getHostProperties(email));
    }

    @PreAuthorize("hasAnyRole('GUEST', 'HOST')")
    @GetMapping("/{id}")
    public ResponseEntity<PropertyResponse> getPropertyById(@PathVariable Long id) {
        return ResponseEntity.ok(propertyService.getPropertyById(id));
    }

    @PreAuthorize("hasRole('HOST')")
    @PutMapping("/{id}")
    public ResponseEntity<PropertyResponse> updateProperty(@PathVariable Long id,
                                                           @RequestBody PropertyRequest request,
                                                           Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(propertyService.updateProperty(id, request, email));
    }

    @PreAuthorize("hasRole('HOST')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProperty(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        propertyService.deleteProperty(id, email);
        return ResponseEntity.ok("Property deleted successfully");
    }
}
