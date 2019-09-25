package com.mos.ticket.booking.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@PropertySource(value = "${CONFIG_LOCATION}")
@EnableScheduling
public class TicketBookingSystemApplication {
	public static void main(String[] args) {
		SpringApplication.run(TicketBookingSystemApplication.class, args);
	}
}
