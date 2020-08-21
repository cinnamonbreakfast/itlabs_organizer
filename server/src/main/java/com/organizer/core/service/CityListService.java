package com.organizer.core.service;

import com.organizer.core.model.CityList;
import com.organizer.core.repository.CityListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityListService {
    CityListRepository cityListRepository;
    @Autowired
    public CityListService(CityListRepository cityListRepository){
        this.cityListRepository=cityListRepository;
    }
    public List<CityList> getCityList(int pageNr,String country,String city)
    {
        Pageable pageable = PageRequest.of(pageNr,5);
        return cityListRepository.getCityList(pageable,country,city).getContent();
    }
    public Long getCount(String country,String city){
        //return cityListRepository.countByCityAndCountry(country,city);
        return 1L;
    }
    public CityList findByCity(String city){
        return cityListRepository.findByCity(city);
    }


}
