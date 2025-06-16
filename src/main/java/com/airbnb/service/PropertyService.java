package com.airbnb.service;

import com.airbnb.dto.PropertyRequest;
import com.airbnb.dto.PropertyResponse;
import com.airbnb.dto.PropertySearchRequest;
import com.airbnb.entity.Property;

import java.util.List;

public interface PropertyService {
	
    PropertyResponse createProperty(PropertyRequest request, String hostEmail);
    List<PropertyResponse> getAllProperties();
    List<PropertyResponse> getHostProperties(String hostEmail);
    PropertyResponse getPropertyById(Long id);
    PropertyResponse updateProperty(Long id, PropertyRequest request, String hostEmail);
    void deleteProperty(Long id, String hostEmail);
    long countAll();
    long countHostProperties(String hostEmail);
    List<Property> searchProperties(PropertySearchRequest request);



}
