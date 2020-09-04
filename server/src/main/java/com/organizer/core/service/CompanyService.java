package com.organizer.core.service;


import com.organizer.core.model.Company;
import com.organizer.core.model.User;
import com.organizer.web.dto.SearchFilter;
import com.organizer.core.repository.CompanyRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CompanyService {
    CompanyRepository companyRepository;

    @Autowired
    public CompanyService(CompanyRepository repository)
    {
        this.companyRepository = repository;
    }

    public List<Company> findByCity(String city){
        return companyRepository.findByCity(city);
    }

    public Company findByName(String name){
        return companyRepository.findByName(name);
    }

    public List<Company> findByCountryAndCity(String city, String country,String company,int pageNumber,int pageSize){
        Pageable page = PageRequest.of(pageNumber,pageSize);
        return this.companyRepository.findByCountryAndCity(page,city,country,company).getContent();
    }
    public Company findById(Long id){
        return companyRepository.findById(id).get();
    }

    public List<Company> findByService(String serviceName,String country, String city,int pageNumber, int pageSize){

        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        return this.companyRepository.findByService(pageable, serviceName, country, city).getContent();
    }

    public List<Company> findByFilter(SearchFilter searchFilter, int pageNumber, int pageSize){
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        return this.companyRepository.findBySearchFilter(pageable,
        searchFilter.getCompanyName(),
        searchFilter.getServiceName(),
        searchFilter.getCountry(),
        searchFilter.getCity()
        ).getContent();
    }

    public Company findByNameAndCity(String name, String city){
        return this.companyRepository.findByNameAndCityAnd(name,city);
    }


    public List<Company> findByOwner(User user )
    {
        return companyRepository.findByOwner(user);
    }

    public Company findByUsername(String username){
        return this.companyRepository.findByUsername(username);
    }

    public Company save(Company company){
        return this.companyRepository.save(company);
    }
    public Company findByUsernameAll(String username){
        return this.companyRepository.findByUsernameAll(username);
    }

}
