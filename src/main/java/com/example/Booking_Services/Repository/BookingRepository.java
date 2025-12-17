package com.example.Booking_Services.Repository;

import com.example.SpringBootEntityService.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking,Long> {

}
