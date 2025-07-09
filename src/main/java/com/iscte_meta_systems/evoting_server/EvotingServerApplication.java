package com.iscte_meta_systems.evoting_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EvotingServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EvotingServerApplication.class, args);
	}

}
