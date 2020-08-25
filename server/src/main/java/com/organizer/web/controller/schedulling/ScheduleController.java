package com.organizer.web.controller.schedulling;

import com.organizer.core.model.*;
import com.organizer.core.service.CompanyService;
import com.organizer.core.service.SpecialistService;
import com.organizer.core.service.UserService;
import com.organizer.core.service.schedulling.AvailabilityService;
import com.organizer.core.service.schedulling.ScheduleService;
import com.organizer.core.service.schedulling.TimeTableService;
import com.organizer.web.auth.JWToken;
import com.organizer.web.dto.BaseDTO;
import com.organizer.web.dto.schedulling.IntervalDTO;
import com.organizer.web.dto.schedulling.ScheduleDTO;
import com.organizer.web.utils.DateOperations;
import org.antlr.v4.runtime.misc.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class ScheduleController {
    AvailabilityService availabilityService;
    TimeTableService timeTableService;
    UserService userService;
    CompanyService companyService;
    DateOperations dateOperations;
    SpecialistService specialistService;
    ScheduleService scheduleService;

    private List<Schedule  > validSchedules(List<TimeTable> availabilities, List<Availability> tt){
        List<Schedule> av = new ArrayList<>(availabilities.size());
        for(TimeTable avf : availabilities) {
            Schedule validSchedule = Schedule.builder().start(avf.getStart()).end(avf.getEnd()).build();
            av.add((validSchedule));
        }
        //add to schedule
        Boolean check = false;
        while(check==false) {
            check= true;
            for(int i =0 ;i<av.size();i++){
                for(int j=0;j<tt.size();j++){
                    Schedule sch = null;
                    if (tt.get(j).getStart().isAfter(av.get(i).getStart()) && tt.get(j).getEnd().isBefore(av.get(i).getEnd())) {
                        //outside
                        sch = Schedule.builder()
                                .start(av.get(i).getStart())
                                .end(tt.get(j).getStart())
                                .build();
                        av.add(sch);
                        sch = Schedule.builder()
                                .start(tt.get(j).getEnd())
                                .end(av.get(i).getEnd())
                                .build();
                        av.add(sch);
                        av.remove(av.get(i));
                        check = false;
                    }
                   else  if ((tt.get(j).getStart().isBefore(av.get(i).getStart())
                            || tt.get(j).getStart().isEqual(av.get(i).getStart()))
                            && (tt.get(j).getEnd().isAfter(av.get(i).getEnd())
                            || tt.get(j).getEnd().isEqual(av.get(i).getEnd())
                    )) {
                        //inside
                        av.remove(av.get(i));
                        check = false;
                    } else if (tt.get(j).getStart().isAfter(av.get(i).getStart())
                            && tt.get(j).getStart().isBefore(av.get(i).getEnd())) {
                        //inside-to the left
                        sch = Schedule.builder()
                                .start(av.get(i).getStart())
                                .end(tt.get(j).getStart())
                                .build();
                        av.remove(av.get(i));
                        av.add(sch);
                        check = false;

                    } else if (tt.get(j).getEnd().isAfter(av.get(i).getStart())&&
                            tt.get(j).getEnd().isBefore(av.get(i).getEnd())
                    ){
                        //inside to the right

                        sch = Schedule.builder()
                                .start(tt.get(j).getEnd())
                                .end(av.get(i).getEnd())
                                .build();
                        av.remove(av.get(i));
                        av.add(sch);
                        check = false;
                    }

                }
            }
        }
        return av;
    }
    @Autowired
    public ScheduleController (TimeTableService timeTableService, UserService userService, CompanyService companyService, DateOperations dateOperations, AvailabilityService availabilityService, SpecialistService specialistService, ScheduleService scheduleService){
        this.timeTableService =timeTableService;
        this.userService=userService;
        this.companyService=companyService;
        this.dateOperations=dateOperations;
        this.availabilityService=availabilityService;
        this.specialistService=specialistService;
        this.scheduleService =scheduleService;
    }
    @RequestMapping(value = "schedule/create", method = RequestMethod.POST)
    public ResponseEntity<String> createSchedule(ScheduleDTO scheduleDTO, @RequestHeader String token){


        Long id = JWToken.checkToken(token);
        if(id==null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not a valid token");
        }

        User user = userService.findById(id);
        if(user==null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not a known user");
        }

        Specialist specialist =specialistService.findById(scheduleDTO.getSpecialistId());
        if(specialist==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not a known specialist");
        }

        if(!((scheduleDTO.getStart() instanceof LocalDateTime) &&( scheduleDTO.getEnd() instanceof  LocalDateTime)))
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong request, not an instance of local date time ");
        }
        if(specialist.getUser().getId()==user.getId()){

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You can't appoint yourself ");
        }

        LocalDateTime start = scheduleDTO.getStart();
        LocalDateTime end = scheduleDTO.getStart().plusSeconds(scheduleDTO.getDuration());

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
        //logic (no collisions)
        Schedule schedule =scheduleService.findCollisions(start,end,user,specialist);
        if(schedule!=null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Found collisions, please select a valid interval");
        }

        //logic : schedule validity from timetable and availability

        List<Availability> availabilities = availabilityService.findByDate(start);
        List<TimeTable> timeTables = timeTableService.findByDate(start);

        List<Schedule> schedules = validSchedules(timeTables,availabilities);

        boolean valid=false;
        for(Schedule schedul: schedules){
            if((start.isAfter(schedul.getStart())|| start.isEqual(schedul.getStart()))&&
            end.isBefore(schedul.getEnd())|| end.isEqual(schedul.getEnd()))
            {
                valid=true;
                break;
            }
        }
        if(valid==false){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid schedule for the service");
        }

        schedule = Schedule.builder()
                .end(end)
                .start(start)
                .specialist(specialist)
                .user(user)
                .build();
        scheduleService.save(schedule);
        return ResponseEntity.ok("Save the schedule ");
    }

    @RequestMapping(value = "specialist/availablity",method = RequestMethod.GET)
    public ResponseEntity<List<IntervalDTO>> getSpecialstIntervalByCompany(IntervalDTO intervalDTO , @RequestHeader String token)
    {

        Long id = JWToken.checkToken(token);
        if(id==null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        User user = userService.findById(id);
        if(user==null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        System.out.println(intervalDTO);
        Specialist specialist =specialistService.findById(intervalDTO.getSpecialist_id());
        if(specialist==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        Service service =specialist.getService();
        LocalDateTime date = intervalDTO.getDate();
        List<Availability> availabilities = availabilityService.findByDate(date);
        List<TimeTable> timeTables = timeTableService.findByDate(date);
        List<Schedule> schedules = validSchedules(timeTables,availabilities);
        List<IntervalDTO> intervalDTOS = new ArrayList<>(schedules.size());
        for(Schedule sch : schedules){
            IntervalDTO inter = IntervalDTO.builder()
                    .start(sch.getStart().toString())
                    .end(sch.getEnd().toString())
                    .build();
            intervalDTOS.add(inter);
        }

        return ResponseEntity.ok(intervalDTOS);
    }
}

