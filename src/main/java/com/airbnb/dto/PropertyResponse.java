package com.airbnb.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyResponse {
	
    private Long id;
    private String title;
    private String description;
    private String location;
    private double pricePerNight;
    private int    maxGuests;
    private String imageUrl;
    private String hostEmail;
    private double averageRating; 

}
