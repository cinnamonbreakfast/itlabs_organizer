package com.organizer.core.repository.schedulling;

import com.organizer.core.model.Availability;
import com.organizer.core.model.Service;
import com.organizer.core.model.Specialist;
import com.organizer.core.repository.Repository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface AvailabilityRepository extends Repository<Long, Availability>{
    @Query("select tt from Availability tt where (tt.start <=?1 and tt.end>= ?2 or " +
            "tt.start>=?1 and tt.start<?2 or "+
            "tt.end>?1 and tt.end<=?2 or "+
            "?1<=tt.start and ?2>=tt.end) and tt.service =?3 and tt.specialist=?4")
    Availability findCollisions(LocalDateTime start, LocalDateTime end, Service service, Specialist specialist);


    @Query("select tt from Availability  tt where date(tt.start)= date(?1) and tt.service=?2")
    List<Availability> findByDate( LocalDateTime date,Service service);
}
