package com.organizer.core.repository.schedulling;

import com.organizer.core.model.*;
import com.organizer.core.repository.Repository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface ScheduleRepository extends Repository<Long, Schedule> {
    @Query("select tt from Schedule tt where tt.start <=?1 and tt.end>= ?2 or " +
            "tt.start>=?1 and tt.start<?2 or "+
            "tt.end>?1 and tt.end<=?2 or "+
            "?1<=tt.start and ?2>=tt.end and tt.user=?3 and tt.specialist=?4")
    Schedule findCollisions(LocalDateTime start, LocalDateTime end, User user ,Specialist specialist);
}
