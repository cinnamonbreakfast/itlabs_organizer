package com.organizer.web.controller;

import com.organizer.core.model.Company;
import com.organizer.core.model.Specialist;
import com.organizer.core.model.User;
import com.organizer.core.service.CompanyService;
import com.organizer.core.service.SpecialistService;
import com.organizer.core.service.UserService;
import com.organizer.web.auth.AuthStore;
import com.organizer.web.dto.CompanyDTO;
import com.organizer.web.dto.SpecialistDTO;
import com.organizer.web.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
public class SpecialistController {
    private final SpecialistService specialistService;
    private final AuthStore authStore;
    private final UserService userService;
    private final CompanyService companyService;

    @Autowired
    public SpecialistController(SpecialistService specialistService,AuthStore authStore,UserService userService, CompanyService companyService){
        this.userService = userService;
        this.specialistService = specialistService;
        this.authStore= authStore;
        this.companyService= companyService;
    }

    @RequestMapping(value = "/specialist/search/{id}", method = RequestMethod.GET)
    public ResponseEntity<Specialist> findSomeSpecialist(@PathVariable Long id) {

        return new ResponseEntity<>(specialistService.findById(id), HttpStatus.OK);
    }


    @RequestMapping(value = "/s/search/by/user/{user_id}", method = RequestMethod.GET)
    public ResponseEntity<Specialist> findSpecialistByPhone(@PathVariable(required= true)Long user_id){

        /*
        if(authStore.sessionExists(token)==false)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Message","Token invalid try log in again").body(null);
*/


        Specialist specialists = specialistService.findById(user_id);
        return ResponseEntity.ok(specialists);
    }
    @RequestMapping(value = "/s/search/by/company/{company_id}", method = RequestMethod.GET)
    public ResponseEntity<List<SpecialistDTO>> findSpecialistsbyCompany(@PathVariable(required= true)Long company_id){
        //TODo : token validation...
        /*
        if(authStore.sessionExists(token)==false)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Message","Token invalid try log in again").body(null);
        */

        List<Specialist> specialists = specialistService.findByCompany(company_id);
        if(specialists!=null) {
            List<SpecialistDTO> specialistDTOS = new ArrayList<>(specialists.size());
            for (Specialist specialist : specialists) {
                User user = specialist.getUser();
                Company company = specialist.getCompany();
                UserDTO userDTO = UserDTO.builder()
                        .city(user.getCity())
                        .country(user.getCountry())
                        .email(user.getEmail())
                        .imageURL(user.getImageURL())
                        .name(user.getName())
                        .phone(user.getPhone())
                        .build();
                userDTO.setId(user.getId());
                CompanyDTO companyDTO = CompanyDTO.builder()
                        .address(company.getAddress())
                        .category(company.getAddress())
                        .city(company.getCity())
                        .name(company.getName())
                        .country(company.getCountry())
                        .build();
                companyDTO.setId(company.getId());
                SpecialistDTO specialistDTO = SpecialistDTO.builder()
                        .user(userDTO)
                        .company(companyDTO)
                        .build();
                specialistDTO.setId(specialist.getId());
                specialistDTOS.add(specialistDTO);
            }

            return ResponseEntity.ok(specialistDTOS);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @RequestMapping(value = "/s/create", method = RequestMethod.POST)
    public ResponseEntity<SpecialistDTO> createSpecialist(@RequestParam Long userId,@RequestParam Long companyId, @RequestHeader(value = "SESSION") String token ) { // this aint c++

//        if(authStore.sessionExists(token)==false)
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Message","Token invalid try log in again").body(null);
//
//        User user = userService.findByEmail(authStore.getUsername(token));
//        if(user==null)
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Message","token invalid").body(null);
//
//        Company company = companyService.findById(companyId);
//        if(company==null)
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Message","Not a known company").body(null);
//
//        if((long)company.getOwner()!=user.getId())
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Message","Not authenticated as an Admin or Company User").body(null);
//
//        Specialist specialist = Specialist.builder()
//                .user(user)
//                .company(company)
//                .build();
//        UserDTO userDTO = UserDTO.builder()
//                .city(user.getCity())
//                .country(user.getCountry())
//                .email(user.getEmail())
//                .imageURL(user.getImageURL())
//                .name(user.getName())
//                .phone(user.getPhone())
//                .build();
//        userDTO.setId(user.getId());
//        CompanyDTO companyDTO = CompanyDTO.builder()
//                .address(company.getAddress())
//                .category(company.getAddress())
//                .city(company.getCity())
//                .name(company.getName())
//                .country(company.getCountry())
//                .build();
//        companyDTO.setId(company.getId());
//        SpecialistDTO specialistDTO = SpecialistDTO.builder()
//                .user(userDTO)
//                .company(companyDTO)
//                .build();
//        specialistDTO.setId(specialist.getId());
//        specialistService.signSpecialist(specialist);
//
//        return ResponseEntity.ok(specialistDTO);
//    }

        return ResponseEntity.ok(null);
    }

    //todo:: seach for a specialist base on user_id
    //todo:: search for a specialist base on company id
    //todo:: validate specialist (based on availability) - important: soon
    //todo:: create a specialist : done
    //todo:: add a specialist an account :
    //todo:: add some kind of regex search in finds  :

}
