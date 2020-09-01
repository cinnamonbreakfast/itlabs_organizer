package com.organizer.core.service.schedulling;

import com.organizer.core.model.Company;
import com.organizer.core.model.Schedule;
import com.organizer.core.model.Specialist;
import com.organizer.core.model.User;
import com.organizer.core.repository.schedulling.ScheduleRepository;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScheduleService {
    ScheduleRepository scheduleRepository;
    @Autowired
    public ScheduleService( ScheduleRepository scheduleRepository){
        this.scheduleRepository = scheduleRepository;

    }
    public Schedule findCollisions(LocalDateTime start, LocalDateTime end, User user , Specialist specialist){
        return scheduleRepository.findCollisions(start,end,user,specialist);
    }

    public List<Schedule> findByUser(User user ){
        return scheduleRepository.findByUser(user);
    }

    public List<Schedule> findBySpecialistAndDate(Specialist specialist, LocalDateTime date ){

        return scheduleRepository.findBySpecialistAndDate(specialist,date);
    }
    public List<Schedule> findAllSchedulesOfUserSpecialistAndCompany(User user , Company company){
        return  scheduleRepository.findAllSchedulesOfUserSpecialistAndCompany(user,company);
    }


    public Schedule findById(Long id ){
        return scheduleRepository.findById(id).get();
    }

    public void save(Schedule schedule){
        scheduleRepository.save(schedule);
    }

    public void delete ( Schedule schedule){
        scheduleRepository.delete(schedule);
    }

}
