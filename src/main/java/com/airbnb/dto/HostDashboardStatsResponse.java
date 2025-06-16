package com.airbnb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HostDashboardStatsResponse {
    private long totalProperties;
    private long totalBookings;
}
