package com.organizer.core.service;


import com.organizer.core.model.Company;
import com.organizer.core.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {
    CompanyRepository companyRepository;

    @Autowired
    public CompanyService(CompanyRepository repository)
    {
        this.companyRepository = repository;
    }


    public List<Company> findByCityAndCategory(String city, String category){
        //return companyRepository.findByCityAndCategory(city, category);
        return null;

    }


    public Company addNewCompany(Company company){
        return companyRepository.save(company);
    }


    public List<Company> findByCity(String city){
        return companyRepository.findByCity(city);
    }

    public Company findByName(String name){
        return companyRepository.findByName(name);
    }

    public List<Company> findByCountryAndCity(String country, String city,int pageNumber){

        Company company = Company.builder()
                .city(city)
                .country(country)
                .build();


        ExampleMatcher customExampleMatcher = ExampleMatcher.matchingAny()
                .withMatcher("city",ExampleMatcher.GenericPropertyMatchers.startsWith() .ignoreCase())
                .withMatcher("country",ExampleMatcher.GenericPropertyMatchers.startsWith().ignoreCase());

        Example<Company> example = Example.of(company,customExampleMatcher);



        Pageable page = PageRequest.of(pageNumber,5);

        
        Page<Company> companyPage =this.companyRepository.findAll(example,page);
        return companyPage.getContent();
    }
}
