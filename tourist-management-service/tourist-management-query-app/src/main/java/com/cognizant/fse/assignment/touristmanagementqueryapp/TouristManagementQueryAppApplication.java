package com.cognizant.fse.assignment.touristmanagementqueryapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class TouristManagementQueryAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(TouristManagementQueryAppApplication.class, args);
	}

}
