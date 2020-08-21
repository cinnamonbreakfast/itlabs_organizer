package com.organizer.web.controller;

import com.organizer.core.model.CityList;
import com.organizer.core.model.Company;
import com.organizer.core.model.CountryList;
import com.organizer.core.model.User;
import com.organizer.core.service.*;
import com.organizer.core.service.file.FileService;
import com.organizer.web.auth.AuthStore;
import com.organizer.web.auth.JWToken;
import com.organizer.web.dto.CompanyDTO;
import com.organizer.web.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CompanyController {


    private final CompanyService companyService;
    private final AuthStore authStore;
    private final UserService userService;
    private final FileService fileService;
    private final AnimeService animeService;
    private final CityListService cityListService;
    private final CountryListService countryListService;
    @Autowired
    public CompanyController(CompanyService companyService, AuthStore authStore, UserService userService, FileService fileService, AnimeService animeService, CityListService cityListService, CountryListService countryListService) {
        this.companyService = companyService;
        this.authStore = authStore;
        this.userService = userService;
        this.fileService=fileService;
        this.animeService=animeService;
        this.cityListService=cityListService;
        this.countryListService=countryListService;
    }

    @RequestMapping(value = "c/create",method = RequestMethod.POST,consumes = {"multipart/form-data"})
        //public ResponseEntity<String> addNewCompany(@RequestParam("file") MultipartFile file, @RequestBody CompanyDTO newCompany, @RequestHeader(name = "token") String token){
    public ResponseEntity<String> addNewCompany(@RequestParam(name="file",required = false) MultipartFile file,
CompanyDTO newCompany,@RequestHeader String token ){

        String mail =  JWToken.checkToken(token);
        //valid token
        if(mail == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");

        String username = newCompany.getName()+"."+newCompany.getCity();
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

        User user = userService.findByEmail(mail);
        company = Company.builder().address(newCompany.getAddress())
                .category(newCompany.getCategory())
                .city(newCompany.getCity())
                .country(newCompany.getCity())
                .owner(user)
                .name(newCompany.getName())
                .username(username)
                .build();

        try {
            String[] list = file.getOriginalFilename().split("[.]");
            if (list.length == 1) {
                fileService.uploadDir(file, company.getImage_url() + "." + list[0]);
                company.setImage_url(username);
            } else if (list.length > 1) {
                fileService.uploadDir(file, company.getImage_url() + "." + list[list.length - 1]);
                company.setImage_url(username);
            } else {
                company.setImage_url("default_company");
            }
        }
        catch (Exception e )
        {
            company.setImage_url("default_company");
        }
        companyService.save(company);
        return ResponseEntity.ok("Created a company");
    }

    @RequestMapping(value = "c/findByOwner",method = RequestMethod.GET)
    public ResponseEntity<List<CompanyDTO>> listByCompanyOwner(@RequestHeader(value = "token") String token){
        String mail = JWToken.checkToken(token);
        if(mail==null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        User user = userService.findByEmail(mail);

        List<Company> companies = companyService.findByOwner(user);

        List<CompanyDTO> companyDTOList = new ArrayList<>(companies.size());
        for(Company company : companies){

            UserDTO userDTO = UserDTO.builder()
                    .phone(user. getPhone())
                    .email(user.getEmail())
                    .build();

            CompanyDTO companyDTO =CompanyDTO.builder()
                    .country(company.getCountry())
                    .name(company.getName())
                    .category(company.getCategory())
                    .address(company.getAddress())
                    .city(company.getCity())
                    .image_url(company.getImage_url())
                    .userDTO(userDTO)
                    .build();
            companyDTOList.add(companyDTO);
        }

        return ResponseEntity.ok(companyDTOList);
    }

    @RequestMapping(value = "c/getId/{id}",method = RequestMethod.GET)
    public ResponseEntity<Company> findCompanyId(@PathVariable Long id ){
        Company company = companyService.findById(id);
        return ResponseEntity.ok(company);
    }
    @RequestMapping(value = "c/{username}",method = RequestMethod.GET)
    public ResponseEntity<Company> findUsername(@PathVariable String username ){
        System.out.println(username);
        Company company = companyService.findByUsername(username);
        if(company==null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        return ResponseEntity.ok(company);
    }
    @RequestMapping(value = "c/changeDetails", method=RequestMethod.PUT)
    public ResponseEntity<String> updateDetails(@RequestParam(name="file",required = false) MultipartFile file,
                                                    CompanyDTO updateCompany,@RequestHeader String token )
    {
            String mail = JWToken.checkToken(token);
            if(mail==null){
                ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not a valid token");
            }
            User user = userService.findByEmail(mail);
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
        try {
                String[] list = file.getOriginalFilename().split("[.]");
                if (list.length == 1) {
                    fileService.uploadDir(file, company.getImage_url() + "." + list[0]);
                    company.setImage_url(username);
                } else if (list.length > 1) {
                    fileService.uploadDir(file, company.getImage_url() + "." + list[list.length - 1]);
                    company.setImage_url(username);
                } else {

                }
            }
            catch (Exception e )
            {

            }
        companyService.save(company);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Updated company");
    }
}
