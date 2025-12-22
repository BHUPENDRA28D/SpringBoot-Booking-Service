package com.example.Booking_Services.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RideRequestDTO {

    private Long passengerId;

/*    private ExactLocation startLocation;
    private ExactLocation endLocation;*/

    private List<Long> driverIds;
}
