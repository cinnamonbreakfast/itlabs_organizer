package com.organizer.web.controller;

import com.organizer.core.model.Company;
import com.organizer.core.model.User;
import com.organizer.core.service.CompanyService;
import com.organizer.core.service.UserService;
import com.organizer.web.auth.AuthStore;
import com.organizer.web.auth.JWToken;
import com.organizer.web.dto.CompanyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @RequestMapping(value = "c/create",method = RequestMethod.POST,consumes = {"multipart/form-data"})
        public ResponseEntity<String> addNewCompany(@RequestParam("uploadedFile") MultipartFile multipart, @RequestBody CompanyDTO newCompany, @RequestHeader(name = "token") String token){
        String mail =  JWToken.checkToken(token);
        if(mail == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");

        Company company = companyService.findByNameAndCity(newCompany.getName(),newCompany.getCity());

        if(company!=null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Company name already taken");

        //valid token
        User user = userService.findByEmail(mail);
        company = Company.builder().address(newCompany.getAddress())
                .category(newCompany.getCategory())
                .city(newCompany.getCity())
                .country(newCompany.getCity())
                .owner(user)
                .name(newCompany.getName())
                .build();



        return ResponseEntity.ok("done");

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
