package com.organizer.web.utils;

import com.organizer.core.model.Schedule;
import com.organizer.core.model.TimeTable;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DateOperations {
    public List<Schedule> convertTtToSchedule(List<TimeTable> tts, LocalDate date ){
        List<Schedule> schedules = new ArrayList<>(tts.size());
        for(TimeTable tt:tts){

            LocalDateTime start = LocalDateTime.of(date,tt.getStart());
            LocalDateTime end = LocalDateTime.of(date,tt.getEnd());
            Schedule sch = Schedule.builder()
                    .start(start)
                    .end(end)
                    .build();
            schedules.add(sch);
        }
        return schedules;
    }

}
