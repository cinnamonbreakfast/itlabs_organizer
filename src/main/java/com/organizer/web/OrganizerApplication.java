package com.organizer.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = {"com.organizer.web.controller", "com.organizer.web.config", "com.organizer.core.repository", "com.organizer.core.model", "com.organizer.core.service", "com.organizer.web.auth"})
@EntityScan("com.organizer.core.model")
public class OrganizerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrganizerApplication.class, args);
    }

}
