package com.organizer.web.controller.schedulling;

import com.organizer.core.model.*;
import com.organizer.core.service.CompanyService;
import com.organizer.core.service.SpecialistService;
import com.organizer.core.service.UserService;
import com.organizer.core.service.schedulling.AvailabilityService;
import com.organizer.core.service.schedulling.TimeTableService;
import com.organizer.web.auth.JWToken;
import com.organizer.web.dto.schedulling.AvailabilityDTO;
import com.organizer.web.dto.schedulling.ScheduleDTO;
import com.organizer.web.utils.DateOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class AvailabilityController {
    AvailabilityService availabilityService;
    TimeTableService timeTableService;
    UserService userService;
    CompanyService companyService;
    DateOperations dateOperations;
    SpecialistService specialistService;
    @Autowired
    public AvailabilityController (TimeTableService timeTableService, UserService userService, CompanyService companyService, DateOperations dateOperations, AvailabilityService availabilityService, SpecialistService specialistService){
        this.timeTableService =timeTableService;
        this.userService=userService;
        this.companyService=companyService;
        this.dateOperations=dateOperations;
        this.availabilityService=availabilityService;
        this.specialistService=specialistService;
    }

    @RequestMapping(value = "availability/create")
    public ResponseEntity<String> createAbailablity(AvailabilityDTO availabilityDTO, @RequestHeader String token){
        Long id = JWToken.checkToken(token);
        if(id==null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not a valid token");
        }

        User user = userService.findById(id);
        if(user==null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not a known user");
        }

        Specialist specialist = specialistService.findByUser(user);
        if(specialist==null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not an specialist account");
        }

        if(!((availabilityDTO.getStart() instanceof LocalDateTime) &&( availabilityDTO.getEnd() instanceof  LocalDateTime)))
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong request, not an instance of local date time ");
        }
        Service service = specialist.getService();

        LocalDateTime start = availabilityDTO.getStart();
        LocalDateTime end = availabilityDTO.getEnd();

        if(start.isAfter(end)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Start time should be before end time");
        }
        //logic

        LocalDateTime end_limit = LocalDateTime.of(
                start.getYear(),
                start.getMonth(),
                start.getDayOfMonth(),23,59,59,99999
        );
        if(end.isAfter(end_limit)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("End time should be within a day of start time");
        }
        Availability availability = availabilityService.findCollisions(start,end,service,specialist);

        if(availability!=null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Found collisions on the interval");
        }

        availability=Availability.builder()
                .end(end)
                .start(start)
                .service(service)
                .specialist(specialist)
                .reason(availabilityDTO.getReason())
                .build();
        availabilityService.save(availability);


        return ResponseEntity.status(HttpStatus.OK).body("Createa an interval for unavailability");
    }


}
