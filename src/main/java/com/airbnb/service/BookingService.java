package com.airbnb.service;

import com.airbnb.dto.BookingRequest;
import com.airbnb.dto.BookingResponse;

import java.util.List;

public interface BookingService {
    BookingResponse createBooking(BookingRequest request, String guestEmail);
    List<BookingResponse> getAllBookings();
    List<BookingResponse> getGuestBookings(String guestEmail);
    List<BookingResponse> getHostBookings(String hostEmail);
    void cancelBooking(Long bookingId, String guestEmail);
	long countAll();
	long countHostBookings(String hostEmail);
	
}
