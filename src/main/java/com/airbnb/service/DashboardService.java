package com.airbnb.service;

public interface DashboardService {
    long countAllUsers();
    long countGuests();
    long countHosts();
    long countAdmins();
    long countAllProperties();
    long countAllBookings();
}
