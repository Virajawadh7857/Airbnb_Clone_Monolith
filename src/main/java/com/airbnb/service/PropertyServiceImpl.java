package com.airbnb.service;

import com.airbnb.dto.PropertyRequest;
import com.airbnb.dto.PropertyResponse;
import com.airbnb.dto.PropertySearchRequest;
import com.airbnb.entity.Property;
import com.airbnb.entity.Review;
import com.airbnb.entity.User;
import com.airbnb.repository.PropertyRepository;
import com.airbnb.repository.ReviewRepository;
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

    @Autowired
    private ReviewRepository reviewRepository;

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
        return propertyRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PropertyResponse> getHostProperties(String hostEmail) {
        User host = userRepository.findByEmail(hostEmail)
                .orElseThrow(() -> new RuntimeException("Host not found"));

        return propertyRepository.findByHost(host)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
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

    private PropertyResponse mapToResponse(Property property) {
        List<Review> reviews = reviewRepository.findByProperty(property);

        double averageRating = reviews.isEmpty() ? 0.0 :
                reviews.stream()
                        .mapToDouble(Review::getRating)
                        .average()
                        .orElse(0.0);

        return PropertyResponse.builder()
                .id(property.getId())
                .title(property.getTitle())
                .description(property.getDescription())
                .location(property.getLocation())
                .pricePerNight(property.getPricePerNight())
                .maxGuests(property.getMaxGuests())
                .imageUrl(property.getImageUrl())
                .hostEmail(property.getHost().getEmail())
                .averageRating(averageRating)
                .build();
    }

    @Override
    public long countAll() {
        return propertyRepository.count();
    }

    @Override
    public long countHostProperties(String hostEmail) {
        User host = userRepository.findByEmail(hostEmail)
                .orElseThrow(() -> new RuntimeException("Host not found"));
        return propertyRepository.countByHost(host);
    }
    
    @Override
    public List<Property> searchProperties(PropertySearchRequest request) {
        if (request.getStartDate() != null && request.getEndDate() != null) {
            return propertyRepository.searchAvailableProperties(
                request.getLocation(),
                request.getMinPrice(),
                request.getMaxPrice(),
                request.getMinGuests(),
                request.getStartDate(),
                request.getEndDate()
            );
        } else {
            return propertyRepository.searchProperties(
                request.getLocation(),
                request.getMinPrice(),
                request.getMaxPrice(),
                request.getMinGuests()
            );
        }
    }


}
