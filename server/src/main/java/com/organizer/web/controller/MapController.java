package com.organizer.web.controller;

import com.organizer.core.model.*;
import com.organizer.core.service.*;
import com.organizer.core.service.SpecialistService;
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
    private final ServiceService specialistServiceService;
    private final AnimeService animeService;
    private final CountryListService countryListService;
    private final CityListService cityListService;
    private final PrefixService prefixService;
    @Autowired
    public MapController(SpecialistService specialistService, CompanyService companyService, ServiceService specialistServiceService, AnimeService animeService, CountryListService countryListService, CityListService cityListService, PrefixService prefixService) {
        this.companyService = companyService;
        this.specialistService = specialistService;
        this.specialistServiceService = specialistServiceService;
        this.animeService= animeService;
        this.countryListService=countryListService;
        this.cityListService=cityListService;
        this.prefixService = prefixService;
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

                List<Service> specialistServices;
                if(type==Type.COMPANY.ordinal()) {
                    try {
                        specialistServices = company.getServices().subList(0, 3);
                    }
                    catch (Exception e){
                        specialistServices = company.getServices();
                    }
                }
                else{
                    try {
                        specialistServices = company.getServices().stream().filter(e -> e.getServiceName().toLowerCase().contains(search_input.toLowerCase())).collect(Collectors.toList()).subList(0, 3);
                    }
                    catch (Exception e){
                        specialistServices = company.getServices().stream().filter(er -> er.getServiceName().toLowerCase().contains(search_input.toLowerCase())).collect(Collectors.toList());
                    }
                }

                List<ServiceDTO> serviceDTOS = new ArrayList<>(specialistServices.size());

                for (Service specialistService : specialistServices) {
                    ServiceDTO serviceDTO = ServiceDTO.builder()
                            .name(specialistService.getServiceName())
                            .duration(specialistService.getDuration())
                            .price(specialistService.getPrice())
                            .build();
                    serviceDTOS.add(serviceDTO);
                }
                CompanyDTO companyDTO = CompanyDTO.builder()
                        .name(company.getName())
                        .address(company.getAddress())
                        .city(company.getCity())
                        .category(company.getCategory())
                        .country(company.getCountry())
                        .services(serviceDTOS)
                        .image_url(company.getImage_url())
                        .username(company.getUsername())
                        .build();
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
        try {
            searchFilter.setCity(searchFilter.getCity().trim());
            searchFilter.setCompanyName(searchFilter.getCompanyName().trim());
            searchFilter.setServiceName(searchFilter.getServiceName().trim());
            searchFilter.setCountry(searchFilter.getCountry().trim());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        List<Company> companies =companyService.findByFilter(searchFilter,searchFilter.getPage(),pageSize );
        List<MapDTO> mapDTOS = new ArrayList<>(companies.size());
        for(Company company : companies) {
            List<Service> specialistServices = company.getServices();

            List<ServiceDTO> serviceDTOS = new ArrayList<>(specialistServices.size());

            for (Service specialistService : specialistServices) {
                ServiceDTO serviceDTO = ServiceDTO.builder()
                        .name(specialistService.getServiceName())
                        .price(specialistService.getPrice())
                        .duration(specialistService.getDuration())
                        .build();
                serviceDTOS.add(serviceDTO);
            }
            CompanyDTO companyDTO = CompanyDTO.builder()
                    .name(company.getName())
                    .address(company.getAddress())
                    .city(company.getCity())
                    .category(company.getCategory())
                    .country(company.getCountry())
                    .username(company.getUsername())
                    .image_url(company.getImage_url())
                    .services(serviceDTOS).build();
            companyDTO.setId(company.getId());

            MapDTO mapDTO = MapDTO.builder()
                    .company(companyDTO)
                    .build();
            mapDTOS.add(mapDTO);
        }
        return  ResponseEntity.ok(mapDTOS);
    }


    @RequestMapping(value="fetch/animelist",method = RequestMethod.GET)
    public ResponseEntity<List<AnimeList>> getAnimeList(@RequestParam int page,@RequestParam String name){
        return ResponseEntity.ok(animeService.getAnimeList(page,name));
    }

    @RequestMapping(value="fetch/countrylist",method = RequestMethod.GET)
    public ResponseEntity<List<CountryList>> getCountryList(@RequestParam int page, @RequestParam String country,@RequestParam String city){
        System.out.println(country+" "+city+"country");
        return ResponseEntity.ok(countryListService.getCountryListByCountry(page,country,city));
    }

    @RequestMapping(value="fetch/citylist",method = RequestMethod.GET)
    public ResponseEntity<List<CityList>> getCityList(@RequestParam int page, @RequestParam String country, @RequestParam String city){
        return ResponseEntity.ok(cityListService.getCityList(page,country,city));
    }
    @RequestMapping(value ="fetch/prefixes",method = RequestMethod.GET)
    public ResponseEntity<List<Prefix>> getPrefixes( ){
        return  ResponseEntity.ok(prefixService.findByCountryOrPrefix());
    }
}
