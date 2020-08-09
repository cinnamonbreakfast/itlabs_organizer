package com.organizer.core.repository;

import com.organizer.core.model.Service;
import com.organizer.core.model.Specialist;

public interface ServiceRepository extends Repository<Long, Service>{
    Service findBySpecialist(Long specialistID);
}
