package com.airbnb.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminDashboardResponse {
    private long totalUsers;
    private long totalGuests;
    private long totalHosts;
    private long totalAdmins;
    private long totalProperties;
    private long totalBookings;
}
