package com.organizer.core.repository;

import com.organizer.core.model.Company;
import com.organizer.core.model.Specialist;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompanyRepository extends Repository<Long, Company>  {
    Company findByName(String name);
    List<Company> findByCity(String city);
    Page<Company> findAll(Example e, Pageable page);

    @Query(value ="select b from Company b where b.city like concat('%',:city,'%') and b.country like concat('%',:country,'%') and b.name like concat('%',:company,'%') ")
    Page<Company>findByCountryAndCity(Pageable pageable, @Param("city")String city ,@Param("country") String country,@Param("company") String company);


    @Query(value = "select distinct c from Company c join Specialist s on c=s.company join SpecialistService sp on sp.specialist=s where sp.serviceName like concat('%',?1,'%') and c.country like concat('%',?2,'%') and c.city like concat('%',?3,'%')" )
    Page<Company>findByService(Pageable pageable,@Param("service_name")String service_name ,@Param("county") String country , @Param("city") String city);

}
