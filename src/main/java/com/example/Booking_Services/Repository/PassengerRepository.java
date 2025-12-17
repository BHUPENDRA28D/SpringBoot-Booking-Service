package com.example.Booking_Services.Repository;

import com.example.SpringBootEntityService.models.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerRepository extends JpaRepository<Passenger,Long>{
}
