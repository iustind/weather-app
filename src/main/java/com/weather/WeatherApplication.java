package com.weather;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WeatherApplication {

	static Logger logger = LoggerFactory.getLogger(WeatherApplication.class);
	
	public static void main(String[] args) {
		// Application Context gets started.
		SpringApplication.run(WeatherApplication.class, args);
	}

}
