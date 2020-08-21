package com.organizer.core.repository;

import com.organizer.core.model.Specialist;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SpecialistRepository extends Repository<Long, Specialist>{
    List<Specialist> findByCompany(Long company);
    Specialist findByUser(Long user);

    @Query(value = "select s from Company c join Specialist s on c=s.company")
    List<Specialist> findByCompanyTest();



    @Query("select s from Company  c join Specialist  s on s.company=c where c.username=?1")
    List<Specialist> findByUsername(String company_username);
}
