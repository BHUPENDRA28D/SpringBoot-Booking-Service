package com.example.Booking_Services.DTO;

import aQute.bnd.annotation.headers.BundleLicense;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DriverLocationDTO {

    String driverId;
    Double latitude;
    Double longitude;
}
