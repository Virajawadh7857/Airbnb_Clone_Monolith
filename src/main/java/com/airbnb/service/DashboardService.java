package com.airbnb.service;

import com.airbnb.dto.AdminDashboardResponse;
import com.airbnb.dto.HostDashboardResponse;
import com.airbnb.dto.DashboardStatsResponse;

public interface DashboardService {
    AdminDashboardResponse getAdminDashboardData();
    DashboardStatsResponse getAdminDashboardStats();
    HostDashboardResponse getHostDashboardData(String email);
}
