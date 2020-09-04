package com.organizer.core.repository;

import com.organizer.core.model.Prefix;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PrefixRepository extends Repository<Long, Prefix>{
    @Query("select  p from Prefix p ")
    List<Prefix> findPrefixByCountryOrPrefix();

    Prefix findPrefixByPrefix(String prefix);
}
