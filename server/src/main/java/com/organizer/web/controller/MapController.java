package com.organizer.web.controller;

import com.organizer.core.model.AnimeList;
import com.organizer.core.model.Company;
import com.organizer.core.model.CountryList;
import com.organizer.core.service.*;
import com.organizer.web.dto.SearchFilter;
import com.organizer.web.dto.CompanyDTO;
import com.organizer.web.dto.ServiceDTO;
import com.organizer.web.dto.map.MapDTO;
import com.organizer.web.utils.Parser;
import com.organizer.web.utils.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class MapController {

    enum Type {
        COMPANY,
        SERVICE
    }

    private final SpecialistService specialistService;
    private final CompanyService companyService;
    private final SpecialistServiceService specialistServiceService;
    private final AnimeService animeService;
    private final CountryListService countryListService;
    @Autowired
    public MapController(SpecialistService specialistService, CompanyService companyService, SpecialistServiceService specialistServiceService,AnimeService animeService,CountryListService countryListService) {
        this.companyService = companyService;
        this.specialistService = specialistService;
        this.specialistServiceService = specialistServiceService;
        this.animeService= animeService;
        this.countryListService=countryListService;
    }

    @RequestMapping(value = "map/sugestion", method = RequestMethod.POST)
    public ResponseEntity<List<MapDTO>> sugestionByType(@RequestParam String search_input, @RequestParam String second_box, @RequestParam int type) {
        String country,city;
        country="";city="";
        try {
            country = StringConverter.converToUpper(second_box.split(",")[1]).substring(0,2);
            city = StringConverter.convertFirstLetterUpper(second_box.split(",")[0]);
        }catch (Exception e){
            city  = StringConverter.convertFirstLetterUpper(second_box);
        }
        List<Company> companies = null;
        if (type == Type.SERVICE.ordinal()) {
            String service_name ="";
            service_name = search_input;
            companies = companyService.findByService(service_name,country,city,0,4);

        } else if (type == Type.COMPANY.ordinal()) {
            String company_name ="";
            company_name=search_input;
            companies = companyService.findByCountryAndCity(city,country,company_name,0,4);
        }
        else{
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
            List<MapDTO> mapDTOS = new ArrayList<>(companies.size());
            for(Company company : companies) {

                List<com.organizer.core.model.SpecialistService> specialistServices;
                if(type==Type.COMPANY.ordinal()) {
                    try {
                        specialistServices = Parser.getServicesFromCompany(company).subList(0, 3);
                    }
                    catch (Exception e){
                        specialistServices = Parser.getServicesFromCompany(company);
                    }
                }
                else{
                    try {
                        specialistServices = Parser.getServicesFromCompany(company).stream().filter(e -> e.getServiceName().toLowerCase().contains(search_input.toLowerCase())).collect(Collectors.toList()).subList(0, 3);
                    }
                    catch (Exception e){
                        specialistServices = Parser.getServicesFromCompany(company).stream().filter(er -> er.getServiceName().toLowerCase().contains(search_input.toLowerCase())).collect(Collectors.toList());
                    }
                }

                List<ServiceDTO> serviceDTOS = new ArrayList<>(specialistServices.size());

                for (com.organizer.core.model.SpecialistService specialistService : specialistServices) {
                    ServiceDTO serviceDTO = ServiceDTO.builder()
                            .name(specialistService.getServiceName()).build();
                    serviceDTOS.add(serviceDTO);
                }
                CompanyDTO companyDTO = CompanyDTO.builder()
                        .name(company.getName())
                        .address(company.getAddress())
                        .city(company.getCity())
                        .category(company.getCategory())
                        .country(company.getCountry())
                        .services(serviceDTOS).build();
                companyDTO.setId(company.getId());

                MapDTO mapDTO = MapDTO.builder()
                        .company(companyDTO)
                        .build();
                mapDTOS.add(mapDTO);
            }
            return ResponseEntity.ok(mapDTOS);
    }


    public static int pageSize = 4;
    @RequestMapping(value="search",method = RequestMethod.GET)
    public ResponseEntity<List<MapDTO>> companySearciveSearchFilter(SearchFilter searchFilter)
    {
        System.out.println(searchFilter);
        List<Company> companies =companyService.findByFilter(searchFilter,searchFilter.getPage(),pageSize );
        List<MapDTO> mapDTOS = new ArrayList<>(companies.size());
        for(Company company : companies) {
            List<com.organizer.core.model.SpecialistService> specialistServices = Parser.getServicesFromCompany(company);

            List<ServiceDTO> serviceDTOS = new ArrayList<>(specialistServices.size());

            for (com.organizer.core.model.SpecialistService specialistService : specialistServices) {
                ServiceDTO serviceDTO = ServiceDTO.builder()
                        .name(specialistService.getServiceName()).build();
                serviceDTOS.add(serviceDTO);
            }
            CompanyDTO companyDTO = CompanyDTO.builder()
                    .name(company.getName())
                    .address(company.getAddress())
                    .city(company.getCity())
                    .category(company.getCategory())
                    .country(company.getCountry())
                    .services(serviceDTOS).build();
            companyDTO.setId(company.getId());

            MapDTO mapDTO = MapDTO.builder()
                    .company(companyDTO)
                    .build();
            mapDTOS.add(mapDTO);
        }
        return  ResponseEntity.ok(mapDTOS);
    }

    @RequestMapping(value="testing",method = RequestMethod.GET)
    public ResponseEntity<List<Company>> testing(){
        List<Company> companies = companies = companyService.findByService("maSaj","Ro","IaSi",0,4);
        return ResponseEntity.ok(companies);    }

    @RequestMapping(value="fetch/animelist",method = RequestMethod.GET)
    public ResponseEntity<List<AnimeList>> getAnimeList(@RequestParam int page,@RequestParam String name){
        return ResponseEntity.ok(animeService.getAnimeList(page,name));
    }

    @RequestMapping(value="fetch/countrylist",method = RequestMethod.GET)
    public ResponseEntity<List<CountryList>> getCountryList(@RequestParam int page, @RequestParam String country){
        return ResponseEntity.ok(countryListService.getCountryListByCountry(page,country));
    }
}
