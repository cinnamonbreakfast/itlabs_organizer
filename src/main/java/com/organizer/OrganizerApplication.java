package com.organizer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication(scanBasePackages = {"com.organizer.controller", "com.organizer.config", "com.organizer.core.repository", "com.organizer.core.model", "com.organizer.core.service"})
@EntityScan("com.organizer.core.model")
public class OrganizerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrganizerApplication.class, args);
    }

}
