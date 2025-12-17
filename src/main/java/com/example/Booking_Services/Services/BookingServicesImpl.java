package com.example.Booking_Services.Services;

import com.example.Booking_Services.DTO.CreateBookingDTO;
import com.example.Booking_Services.DTO.CreateBookingResponseDTO;
import com.example.Booking_Services.DTO.DriverLocationDTO;
import com.example.Booking_Services.DTO.NearbyDriverRequestDTO;
import com.example.Booking_Services.Repository.BookingRepository;
import com.example.Booking_Services.Repository.PassengerRepository;
import com.example.SpringBootEntityService.models.Booking;
import com.example.SpringBootEntityService.models.BookingStatus;
import com.example.SpringBootEntityService.models.Passenger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class BookingServicesImpl implements BookingService {

    private final PassengerRepository passengerRepository;
    private final BookingRepository bookingRepository;
    private final RestTemplate restTemplate;

    //url of locaition servide
    private static  final String LOCATION_SERVICE = "http://localhost:8082";

    public BookingServicesImpl(PassengerRepository passengerRepository, BookingRepository bookingRepository) {
        this.passengerRepository = passengerRepository;
        this.bookingRepository = bookingRepository;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public CreateBookingResponseDTO createBooking(CreateBookingDTO bookingDetails) {

        Optional<Passenger> passenger = passengerRepository.findById(bookingDetails.getPassengerId());



        Booking booking = Booking.builder()
                .bookingStatus(BookingStatus.ASSINGING_DRIVER)
                .startLocation(bookingDetails.getStartLocation())
                .endLocation(bookingDetails.getEndLocation())
                .build();
         Booking newBooking =  bookingRepository.save(booking);

//   making call to location services to find nearby drivers..!

        NearbyDriverRequestDTO requestDTO = NearbyDriverRequestDTO
                .builder()
                .latitude(bookingDetails.getStartLocation().getLatitude())
                .longitude(bookingDetails.getStartLocation().getLongitude())
                .build();

        ResponseEntity<DriverLocationDTO[]> result = restTemplate.postForEntity(LOCATION_SERVICE + "/api/location/nearby/drivers", requestDTO, DriverLocationDTO[].class);


        if (result.getStatusCode().is2xxSuccessful() && result.getBody() != null){
            List<DriverLocationDTO> dirverLocations = Arrays.asList(result.getBody()) ;
            dirverLocations.forEach(driverLocationDTO -> {
                System.out.println(driverLocationDTO
                        .getDriverId()+"  "+" lat : "+driverLocationDTO.getLatitude()+" long: "+driverLocationDTO.getLongitude());
            });

        }



        return  CreateBookingResponseDTO.builder()
                .bookingID(newBooking.getId())
                .bookingStatus(newBooking.getBookingStatus().toString())
                .drive(Optional.of(newBooking.getDriver()))
                .build();
    }
}
