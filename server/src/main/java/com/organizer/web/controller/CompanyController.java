package com.organizer.web.controller;

import com.organizer.core.model.Company;
import com.organizer.core.model.User;
import com.organizer.core.service.CompanyService;
import com.organizer.core.service.UserService;
import com.organizer.web.auth.AuthStore;
import com.organizer.web.dto.CompanyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CompanyController {


    private final CompanyService companyService;
    private final AuthStore authStore;
    private final UserService userService;

    @Autowired
    public CompanyController(CompanyService companyService, AuthStore authStore, UserService userService) {
        this.companyService = companyService;
        this.authStore = authStore;
        this.userService = userService;
    }

    @RequestMapping(value = "c/create",method = RequestMethod.POST)
    public ResponseEntity<String> addNewCompany(@RequestBody CompanyDTO newCompany, @RequestHeader(name = "token") String token){
        Company existingCompany = companyService.findByName(newCompany.getName());
        Long userId = authStore.getUsername(token);
        User owner;

        username != null ? userService.findById(userId) : null;

        if(existingCompany == null){
            Company company = Company.builder()
                    .name(newCompany.getName())
                    .city(newCompany.getCity())
                    .address(newCompany.getAddress())
                    .category(newCompany.getCategory())
                    .country(newCompany.getCountry())
                    .build();

            if(companyService.addNewCompany(company) != null)
                return ResponseEntity.status(HttpStatus.OK).body("Company was successfully added.");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("There was a problem with your registration. Try again later.");
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("This company name is already used.");
    }





    @RequestMapping(value = "c/find",method = RequestMethod.GET)
    public ResponseEntity<List<CompanyDTO>> getFirstFiveBestResults(@RequestParam(required = false) String city,@RequestParam(required = false ) String country,@RequestHeader(value = "SESSION") String token){
        if(!authStore.sessionExists(token))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

        List<Company> companies = null;

        List<CompanyDTO> companyDTOList = companies.stream()
                .map(x->CompanyDTO.builder()
                        .city(x.getCity())
                        .country(x.getCountry())
                        .name(x.getName())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(companyDTOList);
    }

    @RequestMapping(value = "c/getId/{id}",method = RequestMethod.GET)
    public ResponseEntity<Company> findCompanyId(@PathVariable Long id ){
        System.out.println("Here");
        Company company = companyService.findById(id);
        return ResponseEntity.ok(company);
    }
}
