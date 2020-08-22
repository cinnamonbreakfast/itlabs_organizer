package com.organizer.core.repository;

import com.organizer.core.model.Prefix;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface PrefixRepository extends Repository<Long, Prefix>{
    @Query("select  p from Prefix p where lower(p.country) like concat('%',lower(?1),'%') or lower(p.prefix) like concat('%',lower(?1),'%')  ")
    Page<Prefix> findPrefixByCountryOrPrefix(Pageable pageable, String country);
}
