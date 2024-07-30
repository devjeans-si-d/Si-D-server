package org.devjeans.sid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SiDApplication {

	public static void main(String[] args) {
		SpringApplication.run(SiDApplication.class, args);
	}

}
