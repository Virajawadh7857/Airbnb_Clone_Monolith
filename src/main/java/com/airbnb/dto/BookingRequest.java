package com.airbnb.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingRequest {
    private Long propertyId;
    private int guestCount;
    private LocalDate startDate;
    private LocalDate endDate;
}
