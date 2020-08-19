package com.organizer.core.service;

import com.organizer.core.model.CountryList;
import com.organizer.core.repository.CountryListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryListService {
    CountryListRepository countryListRepository;
    @Autowired
    public CountryListService(CountryListRepository countryListRepository){
        this.countryListRepository= countryListRepository;
    }

    public List<CountryList> getCountryListByCountry(int pageNr,String country){
        Pageable pageable = PageRequest.of(pageNr,5);
        return countryListRepository.getCountryListByCountry(pageable,country).getContent();
    }
}
