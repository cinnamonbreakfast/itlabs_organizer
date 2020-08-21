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

    @Query("select c from Company c where c.name=?1 and c.city=?2")
    Company findByNameAndCityAnd(String name,String city);

    List<Company> findByCity(String city);

    Page<Company> findAll(Example e, Pageable page);

    @Query(value ="select b from Company b " +
            "where lower(b.city) like concat('%',lower(?1),'%')" +
            " and lower(b.country) like concat('%',lower(?2),'%') " +
            "and lower(b.name) like concat('%',lower(?3),'%') ")
    Page<Company>findByCountryAndCity(Pageable pageable,String city ,String country, String company);


    //@Query(value = "select distinct c from Company c join Specialist s on c=s.company join SpecialistService sp on sp.specialist=s where sp.serviceName like concat('%',?1,'%') and c.country like concat('%',?2,'%') and c.city like concat('%',?3,'%')" )\
    @Query(value = "select distinct c from Company c join Specialist s on c=s.company join SpecialistService sp on sp.specialist=s where" +
            " lower(sp.serviceName) like concat('%',lower(?1) ,'%') " +
            "and lower(c.country) like concat('%',lower (?2),'%') " +
            "and lower (c.city) like concat('%',lower(?3),'%')")
    Page<Company>findByService(Pageable pageable,String service_name ,String country , String city);

    @Query(value = "select distinct c from Company c join Specialist s on c=s.company join SpecialistService sp on sp.specialist=s " +
            " where lower(c.name) like concat('%',lower(?1),'%') and " +
            "lower(sp.serviceName) like concat('%',lower(?2) ,'%') " +
            "and lower(c.country) like concat('%',lower (?3),'%') " +
            "and lower (c.city) like concat('%',lower(?4),'%')")
    Page <Company> findBySearchFilter(Pageable pageable, String companyName, String serviceName,String country,String city);


}
