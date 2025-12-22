package com.example.Booking_Services.DTO;

import com.example.SpringBootEntityService.models.BookingStatus;
import com.example.SpringBootEntityService.models.Driver;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateBookingResponseDTO {

    private Long bookingId;
    private BookingStatus status;
//    private Optional<Driver> driver;
  private Long driverId;

}
