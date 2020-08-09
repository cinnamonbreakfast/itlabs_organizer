package com.organizer.web.controller;

import com.organizer.core.model.Company;
import com.organizer.core.model.Service;
import com.organizer.core.model.User;
import com.organizer.core.service.CompanyService;
import com.organizer.core.service.ServiceService;
import com.organizer.core.service.UserService;
import com.organizer.web.auth.AuthStore;
import com.organizer.web.dto.CompanyDTO;
import com.organizer.web.dto.ServiceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ServiceController {
    AuthStore authStore;
    UserService userService;
    CompanyService companyService;
    ServiceService serviceService;
    @Autowired
    public ServiceController(AuthStore authStore, UserService userService, CompanyService companyService, ServiceService serviceService){
        this.authStore= authStore;
        this.userService =userService;
        this.companyService = companyService;
        this.serviceService = serviceService;
    }

    //create
    @RequestMapping(value = "/service/create", method = RequestMethod.POST)
    public ResponseEntity<ServiceDTO> createService(@RequestParam Long companyId, @RequestHeader String token, @RequestParam String serviceName){
//        if(authStore.sessionExists(token)==false)
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Message","Token invalid try log in again").body(null);
//        User user = userService.findByEmail(authStore.getUsername(token));
//        Company company = companyService.findById(companyId);
//        if(company.getOwner()!=user.getId())
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Message","Not an owner of the company").body(null);
//
//        Service service = Service.builder()
//                .name(serviceName)
//                .build();
//        CompanyDTO companyDTO = CompanyDTO.builder()
//                .address(company.getAddress())
//                .category(company.getCategory())
//                .name(company.getName())
//                .city(company.getCity())
//                .country(company.getCountry())
//                .build();
//        ServiceDTO serviceDTO = ServiceDTO.builder()
//                .companyDTO(companyDTO)
//                .name(serviceName).build();
//        serviceService.signService(service);

        return ResponseEntity.ok(null);
    }


}

