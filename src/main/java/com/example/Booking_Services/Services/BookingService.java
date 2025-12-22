package com.example.Booking_Services.Services;


import com.example.Booking_Services.DTO.CreateBookingDTO;
import com.example.Booking_Services.DTO.CreateBookingResponseDTO;
import com.example.Booking_Services.DTO.UpdateBookingRequestDTO;
import com.example.Booking_Services.DTO.UpdateBookingResponseDTO;
import com.example.SpringBootEntityService.models.Booking;
import org.springframework.stereotype.Service;


public interface BookingService {


    public CreateBookingResponseDTO createBooking(CreateBookingDTO createBookingDTO);


    public UpdateBookingResponseDTO updateBooking(UpdateBookingRequestDTO bookingRequestDTO,Long bookingId);





}
