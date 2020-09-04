package com.organizer.core.repository;

import com.organizer.core.model.Service;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ServiceRepository extends Repository<Long, Service>{

        @Query(value = "select sp from Service sp where sp.serviceName=?1 and sp.company.username=?2")
        Service findServiceByCompanyAndName( String str, String str1);
}
