package com.organizer.core.utils;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class SearchFilter {

    int page;
    String serviceName;
    String companyName;
    String city;
    String country;
    public SearchFilter(){}
    public SearchFilter(int page, String serviceName, String companyName, String city, String country){
        this.page = page;
        this.serviceName = serviceName;
        this.companyName = companyName;
        this.city = city;
        this.country = country;
    }
}
