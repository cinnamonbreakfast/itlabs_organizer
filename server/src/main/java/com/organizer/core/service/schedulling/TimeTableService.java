package com.organizer.core.service.schedulling;

import com.organizer.core.model.Company;
import com.organizer.core.model.TimeTable;
import com.organizer.core.repository.schedulling.TimeTableRepository;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    public TimeTable findCollisions(LocalDateTime start , LocalDateTime end, com.organizer.core.model.Service service){
        return timeTableRepository.findCollisions(start,end,service);
    }

    public List<TimeTable> findByDate(LocalDateTime date){
        return timeTableRepository.findByDate(date);
    }

    public List<TimeTable> findByCompanyAndRange(Company company, LocalDateTime start,LocalDateTime end){
        if(!((start instanceof LocalDateTime) &&( end instanceof  LocalDateTime)))
        {
            return timeTableRepository.findByCompany(company);
        }
        return timeTableRepository.findByCompanyRange(company,start,end);

    }
}