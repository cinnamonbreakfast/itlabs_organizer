package com.organizer.web.controller.schedulling;

import com.auth0.jwt.JWT;
import com.organizer.core.model.*;
import com.organizer.core.service.CompanyService;
import com.organizer.core.service.SpecialistService;
import com.organizer.core.service.UserService;
import com.organizer.core.service.schedulling.AvailabilityService;
import com.organizer.core.service.schedulling.ScheduleService;
import com.organizer.core.service.schedulling.TimeTableService;
import com.organizer.web.auth.JWToken;
import com.organizer.web.dto.*;
import com.organizer.web.dto.schedulling.IntervalDTO;
import com.organizer.web.dto.schedulling.ScheduleDTO;
import com.organizer.web.utils.DateOperations;
import org.antlr.v4.runtime.misc.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
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

    private List<Schedule  > validSchedules(List<Schedule> availabilities, List<Availability> tt){
        List<Schedule> av = new ArrayList<>(availabilities.size());

        for(Schedule avf : availabilities) {
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

        System.out.println(scheduleDTO);
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

        if(!((scheduleDTO.getStart() instanceof LocalDateTime) ))
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
        //logic collisions

        Schedule schedule = scheduleService.findCollisions(start,end,user,specialist);
        if(schedule!=null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Found collisions, please select a valid interval");
        }

        //logic : schedule validity from timetable and availability
        Service service =specialist.getService();
        List<Availability> availabilities = availabilityService.findByDate(start,service);
        int day = start.getDayOfWeek().getValue();
        List<TimeTable> tts = timeTableService.findByDay(day,service);
        //conver to the same day
        LocalDate date = start.toLocalDate();
        List<Schedule> timeTables =dateOperations.convertTtToSchedule(tts,date);
        List<Schedule> schedules = validSchedules(timeTables,availabilities);
        System.out.println(start);
        System.out.println(end);

        boolean valid=false;
        for(Schedule schedul: schedules){
            System.out.println(schedul.getStart());
            System.out.println(schedul.getEnd());
            if((start.isAfter(schedul.getStart())|| start.isEqual(schedul.getStart()))&&
                    (end.isBefore(schedul.getEnd())|| end.isEqual(schedul.getEnd())))
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

        Specialist specialist =specialistService.findById(intervalDTO.getSpecialist_id());
        if(specialist==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        Service service =specialist.getService();
        LocalDateTime date = intervalDTO.getDate();
        //service
        List<Availability> availabilities = availabilityService.findByDate(date,service);
        int day = date.getDayOfWeek().getValue();
        List<TimeTable> tts = timeTableService.findByDay(day,service);
        //conver to the same day
        LocalDate dat = date.toLocalDate();
        List<Schedule> timeTables =dateOperations.convertTtToSchedule(tts,dat);



        List<Schedule> schedules = validSchedules(timeTables,availabilities);


        List<Schedule> specialistSchedules = scheduleService.findBySpecialistAndDate(specialist,date);


        List<Availability> tmp = new ArrayList<>();
        for( Schedule sh : specialistSchedules){
            Availability availability = Availability.builder()
                    .start(sh.getStart())
                    .end(sh.getEnd())
                    .build();
            tmp.add(availability);
        }


        List<Schedule> schedulesOut = validSchedules(schedules,tmp);

        List<IntervalDTO> intervalDTOS = new ArrayList<>(schedulesOut.size());

        for(Schedule sch : schedulesOut){
            IntervalDTO inter = IntervalDTO.builder()
                    .start(sch.getStart().toString())
                    .end(sch.getEnd().toString())
                    .build();
            intervalDTOS.add(inter);
        }

        return ResponseEntity.ok(intervalDTOS);
    }

    @RequestMapping(value= "schedules/user/display",method = RequestMethod.GET)
    public ResponseEntity<List<ScheduleDTO>> getSchedulesForUser(@RequestHeader String token)
    {
        Long id = JWToken.checkToken(token);
        if(id == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        User user = userService.findById(id);
        if(user == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        List <Schedule > schedules =  scheduleService.findByUser(user);
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>(schedules.size());
        for(Schedule sch : schedules){

            Specialist sp = sch.getSpecialist();
            Service ser = sp.getService();
            User u = sp.getUser();
            UserDTO userDTO = UserDTO.builder()
                    .imageURL(u.getImageURL())
                    .country(u.getCountry())
                    .city(u.getCity())
                    .phone(u.getPhone())
                    .email(u.getEmail())
                    .name(u.getName())
                    .build();
            userDTO.setId(u.getId());

            ServiceDTO serDTo = ServiceDTO.builder()
                    .price(ser.getPrice())
                    .price(ser.getPrice())
                    .name(ser.getServiceName()).build();
            serDTo.setId(ser.getId());
            Company company = ser.getCompany();
            CompanyDTO companyDTO = CompanyDTO.builder()
                    .image_url(company.getImage_url())
                    .username(company.getUsername())
                    .category(company.getCategory())
                    .city(company.getCity())
                    .address(company.getAddress())
                    .name(company.getName())
                    .cui(company.getCui())
                    .country(company.getCountry())
                    .build();
            companyDTO.setId(company.getId());
            SpecialistDTO spDTO = SpecialistDTO.builder()
                    .servicesDTO(serDTo)
                    .user(userDTO)
                    .company(companyDTO)
                    .build();
            spDTO.setId(sp.getId());
            ScheduleDTO scheduleDTO = ScheduleDTO.builder()
                    .s_start(sch.getStart().toString())
                    .s_end(sch.getEnd().toString())
                    .specialistDTO(spDTO)
                    .id(sch.getId())
                    .build();
            scheduleDTOS.add(scheduleDTO);
        }
        return ResponseEntity.ok(scheduleDTOS);
    }

    @RequestMapping(value= "schedules/user_specialist/display",method = RequestMethod.GET)
    public ResponseEntity<List<ScheduleDTO>> getSpecialistSchedules(@RequestHeader String token, @RequestParam String companyUsername) {
        Long id = JWToken.checkToken(token);
        if (id == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        User user = userService.findById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        Company company = companyService.findByUsername(companyUsername);
        System.out.println(user.getId());
        System.out.println(company.getId());
        if (company == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        List<Schedule> schedules = scheduleService.findAllSchedulesOfUserSpecialistAndCompany(user, company);
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>(schedules.size());
        for (Schedule sch : schedules) {
            System.out.println(sch.getEnd());
            System.out.println(sch.getStart());
            Specialist sp = sch.getSpecialist();
            Service ser = sp.getService();
            User u = sp.getUser();
            UserDTO userDTO = UserDTO.builder()
                    .imageURL(u.getImageURL())
                    .country(u.getCountry())
                    .city(u.getCity())
                    .phone(u.getPhone())
                    .email(u.getEmail())
                    .name(u.getName())
                    .build();
            userDTO.setId(u.getId());

            ServiceDTO serDTo = ServiceDTO.builder()
                    .price(ser.getPrice())
                    .price(ser.getPrice())
                    .name(ser.getServiceName()).build();
            serDTo.setId(ser.getId());
            SpecialistDTO spDTO = SpecialistDTO.builder()
                    .servicesDTO(serDTo)
                    .user(userDTO)
                    .build();
            spDTO.setId(sp.getId());
            ScheduleDTO scheduleDTO = ScheduleDTO.builder()
                    .s_start(sch.getStart().toString())
                    .s_end(sch.getEnd().toString())
                    .specialistDTO(spDTO)
                    .build();
            scheduleDTOS.add(scheduleDTO);


        }
        return ResponseEntity.ok(scheduleDTOS);
    }





@RequestMapping(value = "schedule/delete",method = RequestMethod.DELETE)
public ResponseEntity<String> ff(@RequestHeader String token , @RequestParam Long scheduleId){

    Long id = JWToken.checkToken(token);
    if(id==null){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not a valid token");
    }

    User user = userService.findById(id);
    if(user==null){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not a known user");
    }

    Schedule schedule = scheduleService.findById(scheduleId);

    if(schedule==null){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not a known user");
    }
    if(schedule.getUser().getId()!= user.getId()&&schedule.getSpecialist().getUser().getId()!=user.getId()){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("not the owner of the schedule");
    }

    try{
        scheduleService.delete(schedule);
    }catch (Exception e ){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("not the owner of the schedule");
    }
    return ResponseEntity.ok("Deleted a schedule");

}



}

