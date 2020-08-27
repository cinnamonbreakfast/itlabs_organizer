package com.organizer.core.repository.schedulling;

import com.organizer.core.model.Availability;
import com.organizer.core.model.Company;
import com.organizer.core.model.Service;
import com.organizer.core.model.TimeTable;
import com.organizer.core.repository.Repository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public interface TimeTableRepository extends Repository<Long, TimeTable> {

    @Query("select tt from TimeTable tt where tt.start <=?1 and tt.end>= ?2 or " +
            "tt.start>?1 and tt.start<?2 or "+
            "tt.end>?1 and tt.end<  ?2 or "+
            "?1<=tt.start and ?2>=tt.end and tt.service=?3 and tt.day=?4")
    TimeTable findCollisions(LocalTime start, LocalTime end, Service service, int day);

    @Query("select tt from TimeTable  tt where tt.day = ?1 and tt.service=?2")
    List<TimeTable> findByDay(int day, Service service);

    @Query("select tt from TimeTable  tt where tt.service.company=?1")
    List<TimeTable> findByCompany(Company company);

    @Query("select tt from TimeTable  tt where tt.service.company=?1 and tt.start>?2 and tt.end<?3 ")
    List<TimeTable> findByCompanyRange(Company company,LocalDateTime start, LocalDateTime end);

    @Query("select tt from TimeTable  tt where tt.service.Id=?1")
    List<TimeTable> findByService(Long serviceId);
}
