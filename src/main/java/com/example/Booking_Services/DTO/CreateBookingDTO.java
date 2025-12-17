package com.example.Booking_Services.DTO;

import com.example.SpringBootEntityService.models.ExactLocation;
import com.example.SpringBootEntityService.models.Passenger;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateBookingDTO {

    private Long passengerId;

    private ExactLocation startLocation;

    private ExactLocation endLocation;
}
