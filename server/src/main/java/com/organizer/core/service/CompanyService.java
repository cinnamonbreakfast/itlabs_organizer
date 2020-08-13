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

    public Company addNewCompany(Company company){
        return companyRepository.save(company);
    }


    public List<Company> findByCity(String city){
        return companyRepository.findByCity(city);
    }

    public Company findByName(String name){
        return companyRepository.findByName(name);
    }

    public List<Company> findByCountryAndCity(String city, String country,String company,int pageNumber,int pageSize){
        Pageable page = PageRequest.of(pageNumber,pageSize);
        Page<Company> companyPage =this.companyRepository.findByCountryAndCity(page,city,country,company);
        return companyPage.getContent();
    }
    public Company findById(Long id){
        return companyRepository.findById(id).get();

    }



}
