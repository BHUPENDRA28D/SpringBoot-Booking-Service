package com.example.Booking_Services.Services;

import com.example.Booking_Services.API.LocationServiceAPI;
import com.example.Booking_Services.API.UberSocketAPI;
import com.example.Booking_Services.DTO.*;
import com.example.Booking_Services.Mapper.BookingResponseMapper;
import com.example.Booking_Services.Repository.BookingRepository;
import com.example.Booking_Services.Repository.DriverRepository;
import com.example.Booking_Services.Repository.PassengerRepository;
import com.example.SpringBootEntityService.models.Booking;
import com.example.SpringBootEntityService.models.BookingStatus;
import com.example.SpringBootEntityService.models.Driver;
import com.example.SpringBootEntityService.models.Passenger;
import jakarta.persistence.Id;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.awt.print.Book;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BookingServicesImpl implements BookingService {

    private final DriverRepository driverRepository;
    private final PassengerRepository passengerRepository;
    private final BookingRepository bookingRepository;
//    private final RestTemplate restTemplate;
    private LocationServiceAPI locationServiceAPI;

    private UberSocketAPI uberSocketAPI;


    public BookingServicesImpl(UberSocketAPI uberSocketAPI, LocationServiceAPI locationServiceAPI, PassengerRepository passengerRepository, DriverRepository driverRepository, BookingRepository bookingRepository) {
        this.uberSocketAPI = uberSocketAPI;
        this.locationServiceAPI = locationServiceAPI;
        this.passengerRepository = passengerRepository;
        this.driverRepository = driverRepository;
        this.bookingRepository = bookingRepository;
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

        proceesNearByDriverAsyn(requestDTO,bookingDetails.getPassengerId(),newBooking.getId());

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




    private void proceesNearByDriverAsyn(NearbyDriverRequestDTO requestDTO,Long passengerId, Long bookingId) {

        Call<List<DriverLocationDTO>> call =
                locationServiceAPI.getNearByDrivers(requestDTO);

        call.enqueue(new Callback<List<DriverLocationDTO>>() {

            @Override
            public void onResponse(
                    Call<List<DriverLocationDTO>> call,
                    Response<List<DriverLocationDTO>> response) {

                try {
                    Thread.sleep(5000);
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

                    rasieRideRequestAsync(RideRequestDTO.builder().passengerId(passengerId).bookingId(bookingId).build());
                } else {
                    log.error("Request for ride failed: {}", response.message());
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


    /*========================================================================
      Rasing ride request async way.
  =========================================================================
*/

    private void rasieRideRequestAsync(RideRequestDTO requestDTO){

        Call<Boolean> call = uberSocketAPI.getNearByDrivers(requestDTO);

        log.info(call.request().url().toString());

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                System.out.println(response.isSuccessful());
                System.out.println(response.message());
                if (response.isSuccessful() && response.body()!=null) {
                    Boolean result = response.body();
                    log.info("Driver response is : {}", result);
                } else {
                    log.error("Request Failed {}", response.message());
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable throwable) {
                log.error("Socket service call failed", throwable);
            }
        });
    }



 /*========================================================================
       Update Booking after the rider and driver acceptes it.
  =========================================================================
*/

    @Override
    public UpdateBookingResponseDTO updateBooking(UpdateBookingRequestDTO bookingRequestDTO, Long bookingId) {


        System.out.println(bookingRequestDTO.getDriverId().get());
        Optional<Driver> driver = driverRepository.findById(bookingRequestDTO.getDriverId().get());
        // TODO : if(driver.isPresent() && driver.get().isAvailable())
        bookingRepository.updateBookingStatusAndDriverById(bookingId, BookingStatus.SCHEDULED,driver.get());
        // TODO: driverRepository.update -> make it unavailable
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        return BookingResponseMapper.toUpdateResponse(booking);


      /*  return UpdateBookingResponseDTO.builder()
                .bookingId(bookingId)
                .status(booking.get().getBookingStatus())
                .driver(Optional.ofNullable(booking.get().getDriver()))
                .build();*/


    }
}
















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


    //url of locaition servide
//    private static final String LOCATION_SERVICE = "http://localhost:8082";


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
