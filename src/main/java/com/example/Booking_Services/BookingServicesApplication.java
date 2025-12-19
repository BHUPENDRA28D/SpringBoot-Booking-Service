package com.example.Booking_Services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EntityScan("com.example.SpringBootEntityService.models")

@Slf4j
@EnableDiscoveryClient
public class BookingServicesApplication {


	public static void main(String[] args) {


		SpringApplication.run(BookingServicesApplication.class, args);
		log.info("\n This is the good Booking services");
	}

}
