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

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    public ResponseEntity<String> createTimeTable(@RequestBody TimeTableDTO timeTableDTO, @RequestHeader String token){

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
        Service service = serviceService.findByServiceAndCompany(timeTableDTO.getServiceName(),timeTableDTO.getCompanyUsername());
        if(service == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not a valid service ");
        }


       LocalTime start ,end;
        try{
            start = timeTableDTO.getStart();
            end = timeTableDTO.getEnd();
        }catch (Exception e ){
            start =null;
            end = null;
        }


        if(!((start instanceof LocalTime) &&(end instanceof  LocalTime)))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not a time data type");

        if(start.isAfter(end) || start.equals(end)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Start time should be before end time");
        }

        int day = timeTableDTO.getDay();
        if(!(day>=0&&day<=6)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not a valid day");
        }
        List<TimeTable> tts =  timeTableService.findCollisions(start,end,service,day);
        if(tts.size()!=0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Found collisions ");
        }
        TimeTable tt ;
        tt = TimeTable.builder()
                .start(start)
                .end(end)
                .service(service)
                .day(day)
                .build();
        try{
        timeTableService.save(tt);}
        catch (Exception e ){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error ");
        }

        return ResponseEntity.status(HttpStatus.OK).body("Createad timetable");


    }
    @RequestMapping(value = "tt/service/display/{serviceId}",method = RequestMethod.GET)
    public ResponseEntity<List<TimeTableDTO>> getTTbyService(@PathVariable Long serviceId){
        if(serviceId==null||!(serviceId instanceof Long)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        List<TimeTable > tts = timeTableService.findByService(serviceId);
        List<TimeTableDTO> ttsDTO = new ArrayList<>(tts.size());
        for(TimeTable tt : tts) {
            TimeTableDTO timeTableDTO = TimeTableDTO.builder()
                    .s_end(tt.getEnd().toString())
                    .day(tt.getDay())
                    .s_start(tt.getStart().toString()).build();
            timeTableDTO.setId(tt.getId());
            ttsDTO.add(timeTableDTO);
        }
        return  ResponseEntity.ok(ttsDTO);
    }

}
