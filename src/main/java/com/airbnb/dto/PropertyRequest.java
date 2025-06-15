package com.airbnb.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyRequest {
    private String title;
    private String description;
    private String location;
    private double pricePerNight;
    private int maxGuests;
    private String imageUrl;
}
