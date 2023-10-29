package com.cognizant.fse.assignment.touristmanagementapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class TouristManagementAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(TouristManagementAppApplication.class, args);
	}

}
