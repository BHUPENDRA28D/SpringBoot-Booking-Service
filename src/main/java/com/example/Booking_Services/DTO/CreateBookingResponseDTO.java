package com.example.Booking_Services.DTO;


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
public class CreateBookingResponseDTO {

    private long bookingID;
    private String bookingStatus;
    private Optional<Driver> drive;
    private  Double price;
}
