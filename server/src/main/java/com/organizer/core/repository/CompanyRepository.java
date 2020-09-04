package com.organizer.core.repository;

import com.organizer.core.model.Company;
import com.organizer.core.model.Specialist;
import com.organizer.core.model.User;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface CompanyRepository extends Repository<Long, Company>  {
    Company findByName(String name);

    @Query("select c from Company c where c.name=?1 and c.city=?2 and c.validated=true")
    Company findByNameAndCityAnd(String name,String city);


    @Query(value = "select c from Company  c where c.city=?1 and c.validated= true" )
    List<Company> findByCity(String city);


    @Query(value = "select c from Company c where c.validated = true")
    Page<Company> findAll(Example e, Pageable page);

    @Query(value ="select b from Company b " +
            "where lower(b.city) like concat('%',lower(?1),'%')" +
            " and lower(b.country) like concat('%',lower(?2),'%') " +
            "and lower(b.name) like concat('%',lower(?3),'%') and b.validated=true ")
    Page<Company>findByCountryAndCity(Pageable pageable,String city ,String country, String company);

    @Query(value = "select distinct s.company from Service s where lower(s.serviceName) like concat('%',lower(?1) ,'%') " +
            "and lower(s.company.country) like concat('%',lower (?2),'%') " +
            "and   lower (s.company.city) like concat('%',lower(?3),'%') and s.company.validated=true")
    Page<Company>findByService(Pageable pageable,String service_name ,String country , String city);

    @Query(value = "select distinct s.company from Service s where lower(s.company.name) like concat('%',lower(?1),'%' )" +
            " and lower(s.serviceName) like concat('%',lower(?2) ,'%') " +
            "and lower(s.company.country) like concat('%',lower (?3),'%') " +
            "and   lower (s.company.city) like concat('%',lower(?4),'%') and s.company.validated=true")
    Page <Company> findBySearchFilter(Pageable pageable, String companyName, String serviceName,String country,String city);

    @Query(value = "select c from Company c where c.owner=?1 and c.validated=true")
    List<Company> findByOwner(User user);


    @Query( value= "select c from Company c where c.username like ?1 and c.validated=true" )
    Company findByUsername(String username);

    @Query( value= "select c from Company c where c.username like ?1 " )
    Company findByUsernameAll(String username);


}
