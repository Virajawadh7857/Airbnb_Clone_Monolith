package com.airbnb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardStatsResponse {
    private long totalUsers;
    private long totalProperties;
    private long totalBookings;
}
