package com.organizer.core.repository.schedulling;

import com.organizer.core.model.Availability;
import com.organizer.core.model.Service;
import com.organizer.core.model.TimeTable;
import com.organizer.core.repository.Repository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface TimeTableRepository extends Repository<Long, TimeTable> {

    @Query("select tt from TimeTable tt where tt.start <=?1 and tt.end>= ?2 or " +
            "tt.start>=?1 and tt.start<?2 or "+
            "tt.end>?1 and tt.end<=?2 or "+
            "?1<=tt.start and ?2>=tt.end and tt.service=?3")
    TimeTable findCollisions(LocalDateTime start, LocalDateTime end, Service service);

    @Query("select tt from TimeTable  tt where date(tt.start)= date(?1)")
    List<TimeTable> findByDate(LocalDateTime date);
}
