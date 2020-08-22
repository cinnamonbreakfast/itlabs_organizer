package com.organizer.web.controller.android;

import com.organizer.core.model.Company;
import com.organizer.core.model.User;
import com.organizer.core.service.*;
import com.organizer.core.service.file.FileService;
import com.organizer.web.auth.AuthStore;
import com.organizer.web.auth.JWToken;
import com.organizer.web.dto.CompanyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class AndroidCompanyController {
    private final CompanyService companyService;
    private final AuthStore authStore;
    private final   UserService userService;
    private final FileService fileService;
    private final AnimeService animeService;
    private final CityListService cityListService;
    private final CountryListService countryListService;
    @Autowired
    public AndroidCompanyController(CompanyService companyService, AuthStore authStore, UserService userService, FileService fileService, AnimeService animeService, CityListService cityListService, CountryListService countryListService) {
        this.companyService = companyService;
        this.authStore = authStore;
        this.userService = userService;
        this.fileService=fileService;
        this.animeService=animeService;
        this.cityListService=cityListService;
        this.countryListService=countryListService;
    }

    @RequestMapping(value = "c/android/create",method = RequestMethod.POST)
    //public ResponseEntity<String> addNewCompany(@RequestParam("file") MultipartFile file, @RequestBody CompanyDTO newCompany, @RequestHeader(name = "token") String token){
    public ResponseEntity<String> addNewCompany(CompanyDTO newCompany, @RequestHeader String token ){

        Long id =  JWToken.checkToken(token);
        //valid token
        if(id == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");

        String username = newCompany.getName()+"-"+newCompany.getCity();
        Company company = companyService.findByUsername(username);
        // validate company_name
        if(company!=null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Company name already taken");

        //todo:validate image

        //validate category
        String category = newCompany.getCategory();
        if(animeService.getCount(category)!=1){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Give a category from database");
        }
        //validate country & city
        String country = newCompany.getCountry();
        String city = newCompany.getCity();
        if(cityListService.getCount(country,city)!=1){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Give a country and city from database");
        }
        //get category, country & city from db
        System.out.println(newCompany);
        System.out.println(city);
        category = animeService.findByList(category).getList();
        country= countryListService.findByCountry(country).getAbbreviation();
        city = cityListService.findByCity(city).getCity();

        User user = userService.findById(id);
        company = Company.builder().address(newCompany.getAddress())
                .category(category)
                .city(city)
                .country(country)
                .owner(user)
                .name(newCompany.getName())
                .username(username)
                .build();
            company.setImage_url("default_company");

        companyService.save(company);
        return ResponseEntity.ok("Created a company");
    }

    @RequestMapping(value = "c/andoird/changeDetails", method=RequestMethod.PUT)
    public ResponseEntity<String> updateDetails(CompanyDTO updateCompany,@RequestHeader String token )
    {
        Long id  = JWToken.checkToken(token);
        if(id==null){
            ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not a valid token");
        }
        User user = userService.findById(id);
        String username = updateCompany.getUsername();
        Company company = companyService.findByUsername(username);
        if(company==null){
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not a known company");
        }
        if(company.getOwner().getEmail().equals(user.getEmail())==false){
            ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not the owner of the company");
        }
        // validate company_name
        if(company!=null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Company name already taken");

        //todo:validate image

        //validate category
        String category = updateCompany.getCategory();
        if(animeService.getCount(category)!=1){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Give a category from database");
        }
        //validate country & city
        String country = updateCompany.getCountry();
        String city = updateCompany.getCity();
        if(cityListService.getCount(country,city)!=1){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Give a country and city from database");
        }
        //get category, country & city from db
        category = animeService.findByList(category).getList();
        country= countryListService.findByCountry(country).getAbbreviation();
        city = cityListService.findByCity(city).getCity();
        String name = updateCompany.getName();
        String address = updateCompany.getAddress();

        company.setAddress(address);
        company.setName(name);
        company.setCity(city);
        company.setCountry(country);
        company.setCategory(category);

        companyService.save(company);
        return ResponseEntity.ok().body("Updated company");
    }

}
