package com.example.Booking_Services.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NearbyDriverRequestDTO {

    String driverId;
    Double latitude;
    Double longitude;

}
