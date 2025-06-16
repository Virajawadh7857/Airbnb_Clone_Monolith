package com.airbnb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HostDashboardResponse {
    private long totalHostProperties;
    private long totalHostBookings;
}
