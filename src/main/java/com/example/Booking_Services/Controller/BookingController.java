package com.example.Booking_Services.Controller;


import com.example.Booking_Services.DTO.CreateBookingDTO;
import com.example.Booking_Services.DTO.CreateBookingResponseDTO;
import com.example.Booking_Services.DTO.UpdateBookingResponseDTO;
import com.example.Booking_Services.DTO.UpdateBookingRequestDTO;
import com.example.Booking_Services.Services.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/booking")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<CreateBookingResponseDTO> createBooking(@RequestBody CreateBookingDTO createBookingDTO){


        return  new ResponseEntity<>(bookingService.createBooking(createBookingDTO), HttpStatus.CREATED);
    }



    @PostMapping("/{bookingId}")
    public ResponseEntity<UpdateBookingResponseDTO> updateBooking(@RequestBody UpdateBookingRequestDTO requestDto, @PathVariable Long bookingId) {
        return new ResponseEntity<>(bookingService.updateBooking(requestDto, bookingId), HttpStatus.OK);
    }


}
