package com.organizer.core.repository;

import com.organizer.core.model.Company;
import com.organizer.core.model.Specialist;
import com.organizer.core.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SpecialistRepository extends Repository<Long, Specialist>{


    @Query(value = "select s from Specialist s where s.service.company=?1 and s.service.company.validated=true")
    List<Specialist> findByCompany(Company company);
    Specialist findByUser(Long user);

    @Query("select s from Specialist  s where  s.service.company.username=?1 and s.service.company.validated=true")
    List<Specialist> findByUsername(String company_username);


    @Query("select s from Specialist  s  where s.service.serviceName=?1 and s.service.company.username=?2 and s.service.company.validated=true")
    List<Specialist> findByCompanyAndServiceName(String service, String username);


    Specialist findByUser(User user );


}
