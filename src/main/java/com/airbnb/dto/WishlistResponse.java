package com.airbnb.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishlistResponse {
    private Long id;
    private Long propertyId;
    private String propertyTitle;
    private String guestEmail;
}
