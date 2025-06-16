package com.airbnb.service;

import com.airbnb.entity.Role;
import com.airbnb.repository.UserRepository;
import com.airbnb.repository.PropertyRepository;
import com.airbnb.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public long countAllUsers() {
        return userRepository.count();
    }

    @Override
    public long countGuests() {
        return userRepository.countByRole(Role.GUEST);
    }

    @Override
    public long countHosts() {
        return userRepository.countByRole(Role.HOST);
    }

    @Override
    public long countAdmins() {
        return userRepository.countByRole(Role.ADMIN);
    }

    @Override
    public long countAllProperties() {
        return propertyRepository.count();
    }

    @Override
    public long countAllBookings() {
        return bookingRepository.count();
    }
}
