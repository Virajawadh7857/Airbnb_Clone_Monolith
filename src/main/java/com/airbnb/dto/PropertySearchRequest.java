package com.airbnb.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PropertySearchRequest {
    private String location;
    private Integer minPrice;
    private Integer maxPrice;
    private Integer minGuests;
    private LocalDate startDate;
    private LocalDate endDate;
}
