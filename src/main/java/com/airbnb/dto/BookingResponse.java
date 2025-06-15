package com.airbnb.dto;

import com.airbnb.entity.BookingStatus;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponse {
    private Long id;
    private String guestEmail;
    private Long propertyId; 
    private int guestCount;
    private LocalDate startDate;
    private LocalDate endDate;
    private BookingStatus status;
}
