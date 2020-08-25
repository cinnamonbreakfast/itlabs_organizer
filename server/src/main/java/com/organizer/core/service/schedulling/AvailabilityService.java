package com.organizer.core.service.schedulling;

import com.organizer.core.model.Availability;
import com.organizer.core.model.Specialist;
import com.organizer.core.repository.schedulling.AvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AvailabilityService {

    AvailabilityRepository availabilityRepository ;
    @Autowired
    public AvailabilityService(AvailabilityRepository availabilityRepository){
        this.availabilityRepository =availabilityRepository;
    }

    public Availability findCollisions(LocalDateTime start, LocalDateTime end, com.organizer.core.model.Service service, Specialist specialist){
        return availabilityRepository.findCollisions(start,end, service,specialist);
    }

    public List<Availability> findByDate(LocalDateTime date){
        return availabilityRepository.findByDate(date);

    }
    public void save(Availability availability){
        availabilityRepository.save(availability);
    }
}
