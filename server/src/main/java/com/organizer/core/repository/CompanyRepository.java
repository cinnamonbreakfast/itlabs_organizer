package com.organizer.core.repository;

import com.organizer.core.model.Company;

import java.util.List;

public interface CompanyRepository extends Repository<Long, Company> {
    Company findByName(String name);
    List<Company> findByCity(String city);
    List<Company> findByCityAndCategory(String city,String category);

}
