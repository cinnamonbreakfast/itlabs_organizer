package com.organizer.web.controller.schedulling;

import com.organizer.core.model.Company;
import com.organizer.core.model.Service;
import com.organizer.core.model.TimeTable;
import com.organizer.core.model.User;
import com.organizer.core.repository.schedulling.TimeTableRepository;
import com.organizer.core.service.CompanyService;
import com.organizer.core.service.ServiceService;
import com.organizer.core.service.UserService;
import com.organizer.core.service.schedulling.TimeTableService;
import com.organizer.web.auth.JWToken;
import com.organizer.web.dto.ServiceDTO;
import com.organizer.web.dto.schedulling.TimeTableDTO;
import com.organizer.web.utils.DateOperations;
import org.apache.coyote.Response;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class TimeTableController {
    TimeTableService timeTableService;
    UserService userService;
    CompanyService companyService;
    DateOperations dateOperations;
    ServiceService serviceService;
    @Autowired
    public TimeTableController (TimeTableService timeTableService, UserService userService, CompanyService companyService, DateOperations dateOperations, ServiceService serviceService){
        this.timeTableService =timeTableService;
        this.userService=userService;
        this.companyService=companyService;
        this.dateOperations=dateOperations;
        this.serviceService=serviceService;
    }

    @RequestMapping(value="tt/create",method = RequestMethod.POST)
    public ResponseEntity<String> createTimeTable(TimeTableDTO timeTableDTO, @RequestHeader String token){

        Long id = JWToken.checkToken(token);
        if(id==null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not a valid token");
        }

        User user = userService.findById(id);
        if(user==null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not a known user");
        }
        Company company =  companyService.findByUsername(timeTableDTO.getCompanyUsername());
        if(company==null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not a known company");
        }

        if(user.getId()!=company.getOwner().getId()){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not the owner of the company");
        }

        if(!((timeTableDTO.getStart() instanceof LocalDateTime) &&( timeTableDTO.getEnd() instanceof  LocalDateTime)))
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong request, not an instance of local date time ");
        }

        LocalDateTime start = timeTableDTO.getStart();
        LocalDateTime end = timeTableDTO.getEnd();
        if(start.isAfter(end)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Start time should be before end time");
        }

        LocalDateTime end_limit = LocalDateTime.of(
                start.getYear(),
                start.getMonth(),
                start.getDayOfMonth(),23,59,59,99999
        );
        if(end.isAfter(end_limit)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("End time should be within a day of start time");
        }
        //logic part
        Service service = serviceService.findByServiceAndCompany(timeTableDTO.getServiceName(),timeTableDTO.getCompanyUsername());
        if(service==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not a known service name from the company");
        }

        TimeTable timeTable  = timeTableService.findCollisions(start,end,service);
        //TimeTable timeTable=null;
        if(timeTable!=null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Found collisions on table");
        }
        timeTable = TimeTable.builder()
                .start(start)
                .end(end)
                .service(null)
                .build();
        timeTableService.save(timeTable);
        return ResponseEntity.status(HttpStatus.OK).body("Succes creating time table");
    }



    @RequestMapping(value="tt/findAllByCompany" ,method = RequestMethod.GET)
    public ResponseEntity<List<TimeTableDTO>> findAllByCompany( TimeTableDTO timeTableDTO){

        Company company = companyService.findByUsername(timeTableDTO.getCompanyUsername());
        if(company==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        LocalDateTime start,end;
        try{
            start = timeTableDTO.getStart();
            end = timeTableDTO.getEnd();
        }
        catch (Exception e){
            start =null; end = null;
        }
        List<TimeTable> tts=  timeTableService.findByCompanyAndRange(company,start,end);
        List<TimeTableDTO> ttDTOS = new ArrayList<>(tts.size());
        for(TimeTable tt : tts ){
            Service service =tt.getService();

            ServiceDTO serviceDTO = ServiceDTO.builder()
                    .name(service.getServiceName())
                    .duration(service.getDuration())
                    .price(service.getPrice())
                    .build();
            serviceDTO.setId(service.getId());

            TimeTableDTO ttDTO = TimeTableDTO.builder()
                    .s_start(tt.getStart().toString())
                    .s_end(tt.getEnd().toString())
                    .serviceDTO(serviceDTO)
                    .build();
            ttDTO.setId(tt.getId());
            ttDTOS.add(ttDTO);
        }
        return ResponseEntity.ok(ttDTOS);

    }



}
