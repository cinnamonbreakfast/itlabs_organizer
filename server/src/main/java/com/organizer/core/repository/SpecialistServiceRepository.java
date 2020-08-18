package com.organizer.core.repository;

import com.organizer.core.model.Specialist;
import com.organizer.core.model.SpecialistService;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpecialistServiceRepository extends Repository<Long, SpecialistService>{

    @Query(value = "select sp from Company c join Specialist s on c=s.company " +
            "join SpecialistService sp on sp.specialist=s")
    List<SpecialistService> findSpecialistServiceByCompany(@Param("company_id") int company_id);
}
