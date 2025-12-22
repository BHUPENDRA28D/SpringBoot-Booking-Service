package com.example.Booking_Services.API;


import com.example.Booking_Services.DTO.DriverLocationDTO;
import com.example.Booking_Services.DTO.NearbyDriverRequestDTO;
import com.example.Booking_Services.DTO.RideRequestDTO;
import org.springframework.http.ResponseEntity;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

import java.util.List;

public interface UberSocketAPI {

    @POST("api/socket/newRide")

    Call<Boolean> getNearByDrivers(@Body RideRequestDTO dto);

}
