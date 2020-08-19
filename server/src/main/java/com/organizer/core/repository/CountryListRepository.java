package com.organizer.core.repository;

import com.organizer.core.model.AnimeList;
import com.organizer.core.model.CountryList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface CountryListRepository extends  Repository<Long, CountryList> {
    @Query("select c from CountryList c where lower(c.country) like concat('%',lower(?1),'%') ")
    Page<CountryList> getCountryListByCountry(Pageable pageable, String country);
}
