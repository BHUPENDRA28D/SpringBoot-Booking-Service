package com.example.Booking_Services.Mapper;

import com.example.Booking_Services.DTO.UpdateBookingResponseDTO;
import com.example.SpringBootEntityService.models.Booking;

import java.util.Optional;

public class BookingResponseMapper {


        public static UpdateBookingResponseDTO toUpdateResponse(Booking booking) {
            return UpdateBookingResponseDTO.builder()
                    .bookingId(booking.getId())
                    .status(booking.getBookingStatus())
                    .driverId(
                            booking.getDriver() != null
                                    ? booking.getDriver().getId()
                                    : null
                    )
                    .build();
        }


}
