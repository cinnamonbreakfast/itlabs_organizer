package com.organizer.web.controller;

import com.organizer.core.model.Company;
import com.organizer.core.model.Specialist;
import com.organizer.core.service.CompanyService;
import com.organizer.core.service.SpecialistService;
import com.organizer.core.service.SpecialistServiceService;
import com.organizer.web.dto.CompanyDTO;
import com.organizer.web.dto.ServiceDTO;
import com.organizer.web.dto.map.MapDTO;
import com.organizer.web.utils.Parser;
import com.organizer.web.utils.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.directory.SearchResult;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.List;

@RestController
public class MapController {

    enum Type {
        COMPANY,
        SERVICE
    }

    private final SpecialistService specialistService;
    private final CompanyService companyService;
    private final SpecialistServiceService specialistServiceService;

    @Autowired
    public MapController(SpecialistService specialistService, CompanyService companyService, SpecialistServiceService specialistServiceService) {
        this.companyService = companyService;
        this.specialistService = specialistService;
        this.specialistServiceService = specialistServiceService;
    }

    @RequestMapping(value = "map/sugestion/input", method = RequestMethod.POST)
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

                MapDTO mapDTO = MapDTO.builder()
                        .company(companyDTO)
                        .build();
                mapDTOS.add(mapDTO);
            }
            return ResponseEntity.ok(mapDTOS);

    }

    @RequestMapping(value = "map/search", method = RequestMethod.POST)
    public ResponseEntity<String> mainSearch(@RequestParam String search)
    {
        System.out.println(search);
        return ResponseEntity.ok("ook");
    }


    @RequestMapping(value="testing",method = RequestMethod.GET)
    public ResponseEntity<String> testing(){
        return ResponseEntity.ok("ok");    }
}
