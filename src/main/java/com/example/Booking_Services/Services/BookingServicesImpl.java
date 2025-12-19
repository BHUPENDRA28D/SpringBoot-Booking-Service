package com.example.Booking_Services.Services;

import com.example.Booking_Services.API.LocationServiceAPI;
import com.example.Booking_Services.DTO.CreateBookingDTO;
import com.example.Booking_Services.DTO.CreateBookingResponseDTO;
import com.example.Booking_Services.DTO.DriverLocationDTO;
import com.example.Booking_Services.DTO.NearbyDriverRequestDTO;
import com.example.Booking_Services.Repository.BookingRepository;
import com.example.Booking_Services.Repository.PassengerRepository;
import com.example.SpringBootEntityService.models.Booking;
import com.example.SpringBootEntityService.models.BookingStatus;
import com.example.SpringBootEntityService.models.Passenger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BookingServicesImpl implements BookingService {

/*
// Use of  RestClient is done here instead of resttemplete.

    private final RestClient locationRestClient;
    private final PassengerRepository passengerRepository;
    private final BookingRepository bookingRepository;

    public BookingServicesImpl(RestClient locationRestClient, PassengerRepository passengerRepository, BookingRepository bookingRepository) {
        this.locationRestClient = locationRestClient;
        this.passengerRepository = passengerRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public CreateBookingResponseDTO createBooking(CreateBookingDTO bookingDetails) {

        Passenger passenger = passengerRepository
                .findById(bookingDetails.getPassengerId())
                .orElseThrow(() -> new RuntimeException("Passenger not found"));

        Booking booking = Booking.builder()
                .bookingStatus(BookingStatus.ASSIGNING_DRIVER)
                .startLocation(bookingDetails.getStartLocation())
                .endLocation(bookingDetails.getEndLocation())
                .build();

        Booking savedBooking = bookingRepository.save(booking);

        NearbyDriverRequestDTO requestDTO = NearbyDriverRequestDTO.builder()
                .latitude(bookingDetails.getStartLocation().getLatitude())
                .longitude(bookingDetails.getStartLocation().getLongitude())
                .build();

        // FEIGN CALL (SYNC)
        List<DriverLocationDTO> drivers =  locationRestClient
                        .post()
                        .uri("/api/location/nearby/drivers")
                         .body(requestDTO)
                        .retrieve()
                        .body(new ParameterizedTypeReference<List<DriverLocationDTO>>() {});


        if (drivers != null) {
            drivers.forEach(driver ->
                    log.info("\n\n\n" +
                            driver.getDriverId() +
                            " lat=" + driver.getLatitude() +
                            " long=" + driver.getLongitude()
                    )
            );
        }

        return CreateBookingResponseDTO.builder()
                .bookingID(savedBooking.getId())
                .bookingStatus(savedBooking.getBookingStatus().name())
                .build();
    }
    .

    */



    /*  //logging
 //   private static final Logger logger = LoggerFactory.getLogger(BookingServicesImpl.class);
*/

    private final PassengerRepository passengerRepository;
    private final BookingRepository bookingRepository;
    private final RestTemplate restTemplate;
    private LocationServiceAPI locationServiceAPI;

    //url of locaition servide
//    private static final String LOCATION_SERVICE = "http://localhost:8082";


    public BookingServicesImpl(LocationServiceAPI locationServiceAPI, BookingRepository bookingRepository, PassengerRepository passengerRepository) {
        this.locationServiceAPI = locationServiceAPI;
        this.restTemplate =new  RestTemplate();
        this.bookingRepository = bookingRepository;
        this.passengerRepository = passengerRepository;
    }

    @Override
    public CreateBookingResponseDTO createBooking(CreateBookingDTO bookingDetails) {

        Optional<Passenger> passenger = passengerRepository.findById(bookingDetails.getPassengerId());



        Booking booking = Booking.builder()
                .bookingStatus(BookingStatus.ASSIGNING_DRIVER)
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

        proceesNearByDriverAsyn(requestDTO);

/*        ResponseEntity<DriverLocationDTO[]> result = restTemplate.postForEntity(LOCATION_SERVICE + "/api/location/nearby/drivers", requestDTO, DriverLocationDTO[].class);


        if (result.getStatusCode().is2xxSuccessful() && result.getBody() != null){
            List<DriverLocationDTO> dirverLocations = Arrays.asList(result.getBody()) ;
            dirverLocations.forEach(driverLocationDTO -> {
                System.out.println(driverLocationDTO
                        .getDriverId()+"  "+" lat : "+driverLocationDTO.getLatitude()+" long: "+driverLocationDTO.getLongitude());
            });

        }*/



        return  CreateBookingResponseDTO.builder()
                .bookingID(newBooking.getId())
                .bookingStatus(newBooking.getBookingStatus().toString())
//                .drive(Optional.of(newBooking.getDriver()))
                .build();
    }




    private void proceesNearByDriverAsyn(NearbyDriverRequestDTO requestDTO) {

        Call<List<DriverLocationDTO>> call =
                locationServiceAPI.getNearByDrivers(requestDTO);

        call.enqueue(new Callback<List<DriverLocationDTO>>() {

            @Override
            public void onResponse(
                    Call<List<DriverLocationDTO>> call,
                    Response<List<DriverLocationDTO>> response) {

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                if (response.isSuccessful() && response.body() != null) {

                    List<DriverLocationDTO> driverLocations = response.body();

                    driverLocations.forEach(driver -> {
                        System.out.println(
                                driver.getDriverId()
                                        + " lat: " + driver.getLatitude()
                                        + " long: " + driver.getLongitude()
                        );
                    });

                } else {
                    log.error("Request failed: {}", response.message());
                }
            }

            @Override
            public void onFailure(
                    Call<List<DriverLocationDTO>> call,
                    Throwable throwable) {

                log.error("Async call failed", throwable);
            }
        });
    }



}
