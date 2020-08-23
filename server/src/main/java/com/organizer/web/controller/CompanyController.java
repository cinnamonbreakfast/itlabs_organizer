package com.organizer.web.controller;

import com.organizer.core.model.*;
import com.organizer.core.service.*;
import com.organizer.core.service.file.FileService;
import com.organizer.web.auth.AuthStore;
import com.organizer.web.auth.JWToken;
import com.organizer.web.dto.*;
import com.organizer.web.utils.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CompanyController {


    private final CompanyService companyService;
    private final AuthStore authStore;
    private final UserService userService;
    private final FileService fileService;
    private final AnimeService animeService;
    private final CityListService cityListService;
    private final CountryListService countryListService;
    private final InvitationService invitationService;
    @Autowired
    public CompanyController(CompanyService companyService, AuthStore authStore, UserService userService, FileService fileService, AnimeService animeService, CityListService cityListService, CountryListService countryListService, InvitationService invitationService) {
        this.companyService = companyService;
        this.authStore = authStore;
        this.userService = userService;
        this.fileService=fileService;
        this.animeService=animeService;
        this.cityListService=cityListService;
        this.countryListService=countryListService;
        this.invitationService = invitationService;
    }

    @RequestMapping(value = "c/create",method = RequestMethod.POST,consumes = {"multipart/form-data"})
        //public ResponseEntity<String> addNewCompany(@RequestParam("file") MultipartFile file, @RequestBody CompanyDTO newCompany, @RequestHeader(name = "token") String token){
    public ResponseEntity<String> addNewCompany(@RequestParam(name="file",required = false) MultipartFile file,
CompanyDTO newCompany,@RequestHeader String token ){

        Long id =  JWToken.checkToken(token);
        //valid token
        if(id== null)
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
        try {
            String[] list = file.getOriginalFilename().split("[.]");
            if (list.length == 1) {
                fileService.uploadDir(file, username+ "." + list[0]);
                company.setImage_url(username);
            } else if (list.length > 1) {
                fileService.uploadDir(file, username + "." + list[list.length - 1]);
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
        return ResponseEntity.ok("Company has been created, wait for an admin validation");
    }

    @RequestMapping(value = "c/findByOwner",method = RequestMethod.GET)
    public ResponseEntity<List<CompanyDTO>> listByCompanyOwner(@RequestHeader(value = "token") String token){
       Long id  = JWToken.checkToken(token);
        if(id==null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        User user = userService.findById(id);

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
                    .username(company.getUsername())
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
    @RequestMapping(value = "c/{username:.+}",method = RequestMethod.GET)
    public ResponseEntity<CompanyDTO> findUsername(@PathVariable String username ){

        Company company = companyService.findByUsername(username);
        if(company==null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        List<com.organizer.core.model.SpecialistService> specialistServices = Parser.getServicesFromCompany(company);

        List<ServiceDTO> serviceDTOS = new ArrayList<>(specialistServices.size());

        for (com.organizer.core.model.SpecialistService specialistService : specialistServices) {
            ServiceDTO serviceDTO = ServiceDTO.builder()
                    .name(specialistService.getServiceName()).build();
            serviceDTOS.add(serviceDTO);
        }
      List<SpecialistDTO> specialistDTOS = Parser.getSpecialisDTOtFromCompany(company);
        CompanyDTO companyDTO = CompanyDTO.builder()
                .name(company.getName())
                .address(company.getAddress())
                .city(company.getCity())
                .category(company.getCategory())
                .country(company.getCountry())
                .username(company.getUsername())
                .specialistDTOList(specialistDTOS)
                .services(serviceDTOS).build();
        companyDTO.setId(company.getId());
        return ResponseEntity.ok(companyDTO);
    }
    @RequestMapping(value = "c/changeDetails", method=RequestMethod.PUT)
    public ResponseEntity<String> updateDetails(@RequestParam(name="file",required = false) MultipartFile file,
                                                    CompanyDTO updateCompany,@RequestHeader String token )
    {
            Long id = JWToken.checkToken(token);
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
        try {
                String[] list = file.getOriginalFilename().split("[.]");
                if (list.length == 1) {
                    fileService.uploadDir(file, username+ "." + list[0]);
                    company.setImage_url(username);
                } else if (list.length > 1) {
                    fileService.uploadDir(file, username + "." + list[list.length - 1]);
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
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Updated company");
    }

    @RequestMapping(value="c/invite/specialist", method = RequestMethod.POST)
    public ResponseEntity<String > inviteSpecialist(@RequestBody Long userId,@RequestBody String companyUsername,@RequestBody String serviceName, @RequestHeader String token){
        Long id = JWToken.checkToken(token);
        if(id==null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bad token");
        }
        User user = userService.findById(id);
        if(user==null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not a known entry user");
        }
        Company company = companyService.findByUsername(companyUsername);
        if(company.getOwner().getId()!=user.getId()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You are not the owner of this company");
        }
        User userSelected = userService.findById(userId);
        if(userSelected==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not a known selected user");
        }
        Invitation invitation =Invitation.builder()
                .company(company)
                .user(userSelected)
                .serviceName(serviceName)
                .build();

        try {
            invitationService.save(invitation);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Server error");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Invitation has been send");
        //todo:mail and phone sending

    }
    //invite specialists



}
