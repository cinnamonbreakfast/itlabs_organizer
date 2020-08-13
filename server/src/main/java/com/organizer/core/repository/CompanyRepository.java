package com.organizer.core.repository;

import com.organizer.core.model.Company;
import com.organizer.core.model.Specialist;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompanyRepository extends Repository<Long, Company> {
    Company findByName(String name);
    List<Company> findByCity(String city);
    Page<Company> findAll(Example e, Pageable page);

    @Query(value ="select b from Company b where b.city like concat('%',:city,'%') and b.country like concat('%',:country,'%') and b.name like concat('%',:company,'%') ")
    //@Query(value ="select b from Company b  ")
    Page<Company>findByCountryAndCity(Pageable pageable, @Param("city")String city ,@Param("country") String country,@Param("company") String company);

}
