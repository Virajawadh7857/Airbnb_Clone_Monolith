package com.airbnb.service;

import com.airbnb.dto.BookingRequest;
import com.airbnb.dto.BookingResponse;
import com.airbnb.entity.*;
import com.airbnb.repository.BookingRepository;
import com.airbnb.repository.PropertyRepository;
import com.airbnb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Override
    public BookingResponse createBooking(BookingRequest request, String guestEmail) {
        User guest = userRepository.findByEmail(guestEmail)
                .orElseThrow(() -> new RuntimeException("Guest not found"));

        Property property = propertyRepository.findById(request.getPropertyId())
                .orElseThrow(() -> new RuntimeException("Property not found"));

        // Check for booking conflict
        boolean hasConflict = bookingRepository
                .existsByPropertyAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        property, request.getEndDate(), request.getStartDate());

        if (hasConflict) {
            throw new RuntimeException("This property is already booked during the selected dates.");
        }

        // Validate guest count
        if (request.getGuestCount() > property.getMaxGuests()) {
            throw new RuntimeException("Guest count exceeds property's max limit");
        }

        Booking booking = Booking.builder()
                .guest(guest)
                .property(property)
                .guestCount(request.getGuestCount())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(BookingStatus.PENDING)
                .build();

        Booking saved = bookingRepository.save(booking);
        return mapToResponse(saved);
    }

    @Override
    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingResponse> getGuestBookings(String guestEmail) {
        User guest = userRepository.findByEmail(guestEmail)
                .orElseThrow(() -> new RuntimeException("Guest not found"));

        return bookingRepository.findByGuest(guest)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingResponse> getHostBookings(String hostEmail) {
        User host = userRepository.findByEmail(hostEmail)
                .orElseThrow(() -> new RuntimeException("Host not found"));

        List<Property> properties = propertyRepository.findByHost(host);

        return properties.stream()
                .flatMap(property -> bookingRepository.findByProperty(property).stream())
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void cancelBooking(Long bookingId, String guestEmail) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getGuest().getEmail().equals(guestEmail)) {
            throw new RuntimeException("You are not authorized to cancel this booking");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }

    @Override
    public long countAll() {
        return bookingRepository.count();
    }

    @Override
    public long countHostBookings(String hostEmail) {
        User host = userRepository.findByEmail(hostEmail)
                .orElseThrow(() -> new RuntimeException("Host not found"));

        return bookingRepository.countByProperty_Host(host);
    }

    private BookingResponse mapToResponse(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .guestEmail(booking.getGuest().getEmail())
                .propertyId(booking.getProperty().getId())
                .guestCount(booking.getGuestCount())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .status(booking.getStatus())
                .build();
    }
}
