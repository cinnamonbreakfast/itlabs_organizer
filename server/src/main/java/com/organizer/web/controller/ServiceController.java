package com.organizer.web.controller;

import com.organizer.core.model.Company;
import com.organizer.core.model.Service;
import com.organizer.core.model.User;
import com.organizer.core.service.*;
import com.organizer.core.service.file.FileService;
import com.organizer.web.auth.AuthStore;
import com.organizer.web.auth.JWToken;
import com.organizer.web.dto.ServiceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class ServiceController {



    private final CompanyService companyService;
    private final AuthStore authStore;
    private final UserService userService;
    private final FileService fileService;
    private final AnimeService animeService;
    private final CityListService cityListService;
    private final CountryListService countryListService;
    private final InvitationService invitationService;
    private final ServiceService serviceService;
    @Autowired
    public ServiceController(CompanyService companyService, AuthStore authStore, UserService userService, FileService fileService, AnimeService animeService, CityListService cityListService, CountryListService countryListService, InvitationService invitationService
    ,ServiceService serviceService) {
        this.companyService = companyService;
        this.authStore = authStore;
        this.userService = userService;
        this.fileService=fileService;
        this.animeService=animeService;
        this.cityListService=cityListService;
        this.countryListService=countryListService;
        this.invitationService = invitationService;
        this.serviceService = serviceService;
    }

    @RequestMapping(value = "service/create", method = RequestMethod.POST)
    public ResponseEntity<String> createService(@RequestHeader String token, @RequestBody ServiceDTO serviceDTO)
    {
        System.out.println(serviceDTO);
        Long id = JWToken.checkToken(token);
        if(id==null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bad token");
        }
        User user = userService.findById(id);
        if(user == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not a known user");
        }
        Company company =  companyService.findByUsername(serviceDTO.getCompanyUsername());
        if(company == null)
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not a valid company");
        }

        if(user.getId()!=company.getOwner().getId())
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not the owner of the company");
        }
        String serviceName =serviceDTO.getName();


        Service service = serviceService.findByServiceAndCompany(serviceName,serviceDTO.getCompanyUsername());
        if(service != null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Service Already Exists");
        }
        try {
            service = Service.builder()
                    .serviceName(serviceDTO.getName())
                    .price(serviceDTO.getPrice())
                    .duration(serviceDTO.getDuration())
                    .company(company)
                    .build();
            serviceService.save(service);
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Created a service");
    }
    @RequestMapping(value = "service/update", method = RequestMethod.PUT)
    public ResponseEntity<String> updateService(@RequestHeader String token ,@RequestBody ServiceDTO serviceDTO){

        System.out.println(serviceDTO);
        Long id = JWToken.checkToken(token);
        if(id==null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bad token");
        }
        User user = userService.findById(id);
        if(user == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not a known user");
        }

        System.out.println(serviceDTO);
        Service service = serviceService.findById(serviceDTO.getId());

        if(service == null)
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not a valid service");
        }

        Company company = service.getCompany();
        if(user.getId()!=company.getOwner().getId())
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not the owner of the company");
        }
        String serviceName =serviceDTO.getName();
            //Service service = serviceService.findByServiceAndCompany(serviceName,serviceDTO.getCompanyUsername());
        try {
            service.setDuration(serviceDTO.getDuration());
        }
        catch (Exception e){

        }
        try {
            service.setPrice(serviceDTO.getPrice());
        }
        catch (Exception e){

        }
        try {
            service.setServiceName(serviceDTO.getName());
        }
        catch (Exception e){

        }

        serviceService.save(service);
        return ResponseEntity.status(HttpStatus.OK).body("Updated service");

    }

    @RequestMapping(value = "service/delete", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteService(@RequestHeader String token ,@RequestBody ServiceDTO serviceDTO){


        System.out.println(serviceDTO);
        Long id = JWToken.checkToken(token);
        if(id==null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bad token");
        }
        User user = userService.findById(id);
        if(user == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not a known user");
        }

        System.out.println(serviceDTO);
        Service service = serviceService.findById(serviceDTO.getId());

        if(service == null)
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not a valid service");
        }

        Company company = service.getCompany();
        if(user.getId()!=company.getOwner().getId())
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not the owner of the company");
        }

        serviceService.delete(service);
        return ResponseEntity.status(HttpStatus.OK).body("Updated service");
    }




}
