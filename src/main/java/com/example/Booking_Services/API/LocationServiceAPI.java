package com.example.Booking_Services.API;

import com.example.Booking_Services.DTO.DriverLocationDTO;
import com.example.Booking_Services.DTO.NearbyDriverRequestDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

import java.util.List;

public interface LocationServiceAPI {

    @POST("/api/location/nearby/drivers")
    Call<List<DriverLocationDTO>> getNearByDrivers(@Body NearbyDriverRequestDTO nearbyDriverRequestDTO);
}
