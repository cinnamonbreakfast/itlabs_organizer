package com.organizer.core.repository;

import com.organizer.core.model.CityList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface CityListRepository extends Repository<Long, CityList>{
    @Query("select distinct l from CityList l join CountryList c on c.abbreviation=l.country where lower(c.country) like concat('%',lower(?1),'%') and lower(l.city) like concat('%',lower(?2),'%')  ")
    public Page<CityList> getCityList(Pageable pageable, String country,String city);
    @Query("select count(l) from CityList l join CountryList c on c.abbreviation=l.country where lower(c.country) like concat('%',lower(?1),'%') and lower(l.city) like concat('%',lower(?2),'%') ")
    Long countByCityAndCountry(String country,String city);

    CityList findByCity(String city);
}
