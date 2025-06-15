package com.airbnb.service;

import com.airbnb.dto.PropertyRequest;
import com.airbnb.dto.PropertyResponse;
import com.airbnb.entity.Property;
import com.airbnb.entity.User;
import com.airbnb.repository.PropertyRepository;
import com.airbnb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PropertyServiceImpl implements PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public PropertyResponse createProperty(PropertyRequest request, String hostEmail) {
        User host = userRepository.findByEmail(hostEmail)
                .orElseThrow(() -> new RuntimeException("Host not found"));

        Property property = Property.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .location(request.getLocation())
                .pricePerNight(request.getPricePerNight())
                .maxGuests(request.getMaxGuests()) 
                .imageUrl(request.getImageUrl())
                .host(host)
                .build();

        Property saved = propertyRepository.save(property);

        return mapToResponse(saved);
    }

    @Override
    public List<PropertyResponse> getAllProperties() {
        return propertyRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PropertyResponse> getHostProperties(String hostEmail) {
        User host = userRepository.findByEmail(hostEmail)
                .orElseThrow(() -> new RuntimeException("Host not found"));

        return propertyRepository.findByHost(host).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private PropertyResponse mapToResponse(Property property) {
        return PropertyResponse.builder()
                .id(property.getId())
                .title(property.getTitle())
                .description(property.getDescription())
                .location(property.getLocation())
                .pricePerNight(property.getPricePerNight())
                .maxGuests(property.getMaxGuests())
                .imageUrl(property.getImageUrl())
                .hostEmail(property.getHost().getEmail())
                .build();
    }
    
    @Override
    public PropertyResponse getPropertyById(Long id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found"));
        return mapToResponse(property);
    }

    @Override
    public PropertyResponse updateProperty(Long id, PropertyRequest request, String hostEmail) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        if (!property.getHost().getEmail().equals(hostEmail)) {
            throw new RuntimeException("You are not authorized to update this property");
        }

        property.setTitle(request.getTitle());
        property.setDescription(request.getDescription());
        property.setLocation(request.getLocation());
        property.setPricePerNight(request.getPricePerNight());
        property.setMaxGuests(request.getMaxGuests());
        property.setImageUrl(request.getImageUrl());

        Property updated = propertyRepository.save(property);
        return mapToResponse(updated);
    }

    @Override
    public void deleteProperty(Long id, String hostEmail) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        if (!property.getHost().getEmail().equals(hostEmail)) {
            throw new RuntimeException("You are not authorized to delete this property");
        }

        propertyRepository.delete(property);
    }

}
