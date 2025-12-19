package com.example.Booking_Services.Clients;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    RestClient locationRestClient(){
        return RestClient.builder()
                .baseUrl("http://localhost:8082")
                .build();
    }
}
