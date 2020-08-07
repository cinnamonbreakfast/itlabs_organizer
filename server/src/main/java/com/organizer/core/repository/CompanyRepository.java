package com.organizer.core.repository;

import com.organizer.core.model.Company;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CompanyRepository extends Repository<Long, Company> {
    Company findByName(String name);
    List<Company> findByCity(String city);
    Page<Company> findAll(Example e, Pageable page);

}
