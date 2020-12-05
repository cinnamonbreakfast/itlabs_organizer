package com.organizer.core.service.schedulling;

import com.organizer.core.model.Company;
import com.organizer.core.model.TimeTable;
import com.organizer.core.repository.schedulling.TimeTableRepository;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Service
public class TimeTableService {
    TimeTableRepository timeTableRepository;
    @Autowired
   public TimeTableService(TimeTableRepository timeTableRepository){
        this.timeTableRepository= timeTableRepository;
    }

    public void save(TimeTable timeTable){
        timeTableRepository.save(timeTable);
    }
    public List<TimeTable> findCollisions(LocalTime start , LocalTime end, com.organizer.core.model.Service service, int day){
        return timeTableRepository.findCollisions(start,end,service,day);
    }

    public List<TimeTable> findByDay(int day, com.organizer.core.model.Service service){
        return timeTableRepository.findByDay(day,service);
    }

    public List<TimeTable> findByCompanyAndRange(Company company, LocalDateTime start,LocalDateTime end){
        if(!((start instanceof LocalDateTime) &&( end instanceof  LocalDateTime)))
        {
            return timeTableRepository.findByCompany(company);
        }
        return timeTableRepository.findByCompanyRange(company,start,end);
    }
    public List<TimeTable> findByService(Long serviceId){
        return timeTableRepository.findByService(serviceId);
    }
    public TimeTable findById( Long id ){
        return timeTableRepository.findById(id).get();
    }
    public void delete(TimeTable timeTable){
        timeTableRepository.delete(timeTable);
    }
}