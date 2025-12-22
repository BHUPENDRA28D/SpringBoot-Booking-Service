package com.example.Booking_Services.DTO;


import com.example.SpringBootEntityService.models.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.swing.text.html.Option;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateBookingRequestDTO {

       private String status;
       private Optional<Long> driverId;
}
