package com.organizer.core.service;


import com.organizer.core.model.Company;
import com.organizer.core.repository.CompanyRepository;

import com.organizer.web.utils.StringConverter;
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

    public List<Company> findByService(String serviceName,String country, String city,int pageNumber, int pageSize){

        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        if(serviceName.length()==0 ){
            return this.companyRepository.findByService(pageable, serviceName, country, city).getContent();
        }
        Page<Company> fistCheck = this.companyRepository.findByService(pageable, serviceName, country, city);
        if(fistCheck.getSize()>1){
            return fistCheck.getContent();
        }
        StringConverter stringConverter = new StringConverter(serviceName.toLowerCase());
        Set<Company> companies = new LinkedHashSet<>(pageSize);
        while(stringConverter.hasNext()&& companies.size()!=pageSize) {
            String service = stringConverter.getPermutation();
            Page<Company> companyPage = this.companyRepository.findByService(pageable, service, country, city);
            companyPage.getContent().stream().forEach(e->{companies.add(e);});
        }
        return new ArrayList<>(companies);

    }



}
