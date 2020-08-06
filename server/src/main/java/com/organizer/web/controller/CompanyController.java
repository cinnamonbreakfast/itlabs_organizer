package com.organizer.web.controller;

import com.organizer.core.model.Company;
import com.organizer.core.service.CompanyService;
import com.organizer.web.auth.AuthStore;
import com.organizer.web.dto.CompanyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CompanyController {


    private final CompanyService companyService;
    private final AuthStore authStore;

    @Autowired
    public CompanyController(CompanyService companyService, AuthStore authStore) {
        this.companyService = companyService;
        this.authStore = authStore;
    }

    @RequestMapping(value = "c/create",method = RequestMethod.POST)
    public ResponseEntity<String> addNewCompany(String name,String city,String address, String category){
        Company existingCompany = companyService.findByName(name);

        if(existingCompany == null){
            Company company = Company.builder()
                    .name(name)
                    .address(address)
                    .category(category)
                    .city(city)
                    .build();

            if(companyService.addNewCompany(company) != null)
                return ResponseEntity.status(HttpStatus.OK).body("Reqistration complete.");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("There was a problem with your registration. Try again later.");
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("This name of the company is already used.");
    }





    @RequestMapping(value = "c/find",method = RequestMethod.GET)
    public ResponseEntity<List<CompanyDTO>> getFirstFiveBestResults(@RequestParam(required = false) String city,@RequestParam(false = true) String country,@RequestHeader(value = "SESSION") String token){
        if(!authStore.sessionExists(token))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);




        List<Company> companies = companyService.findByCountryAndCity(country,city,0);

        List<CompanyDTO> companyDTOList = companies.stream()
                .map(x->CompanyDTO.builder()
                        .city(x.getCity())
                        .country(x.getCountry())
                        .name(x.getName())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(companyDTOList);

    }


}
