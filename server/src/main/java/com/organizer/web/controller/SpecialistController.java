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
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.provider.HibernateUtils;
import org.springframework.data.jpa.repository.query.Jpa21Utils;
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

    @RequestMapping(value = "/s/search/by/user/{user_id}", method = RequestMethod.GET)
    public ResponseEntity<List<SpecialistDTO>> findSpecialistByPhone(@PathVariable(required= true)Long user_id){

        /*
        if(authStore.sessionExists(token)==false)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Message","Token invalid try log in again").body(null);

         */
        List<Specialist> specialists = specialistService.findById(user_id);
        List<SpecialistDTO> specialistDTOS = new ArrayList<>(specialists.size());
        if(specialists!=null){
            for(Specialist specialist: specialists) {
                User user = specialist.getUser();
                Company company = specialist.getCompany();

                SpecialistDTO specialistDTO = SpecialistDTO.builder()
                        .specialistName(user.getName())
                        .specialistPhone(user.getPhone())
                        .specialistEmail(user.getEmail())
                        .specialistCountry(user.getCountry())
                        .specialistCity(user.getCity())
                        .specialistImageURL(user.getImageURL())
                        .companyAddress(company.getAddress())
                        .companyCategory(company.getCategory())
                        .companyCity(company.getCity())
                        .companyCountry(company.getCountry())
                        .companyId(company.getId())
                        .userId(user.getId())
                        .companyName(company.getName())
                        .build();
                specialistDTO.setId(specialist.getId());
                specialistDTOS.add(specialistDTO);

                return ResponseEntity.ok(specialistDTOS);
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
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
                SpecialistDTO specialistDTO = SpecialistDTO.builder()
                        .specialistName(user.getName())
                        .specialistPhone(user.getPhone())
                        .specialistEmail(user.getEmail())
                        .specialistCountry(user.getCountry())
                        .specialistCity(user.getCity())
                        .specialistImageURL(user.getImageURL())
                        .companyAddress(company.getAddress())
                        .companyCategory(company.getCategory())
                        .companyCity(company.getCity())
                        .companyCountry(company.getCountry())
                        .companyId(company.getId())
                        .companyName(company.getName())
                        .userId(user.getId())
                        .build();
                specialistDTO.setId(specialist.getId());
                specialistDTOS.add(specialistDTO);
            }

            return ResponseEntity.ok(specialistDTOS);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @RequestMapping(value = "/s/create", method = RequestMethod.POST)
    public ResponseEntity<SpecialistDTO> createSpecialist(@PathVariable Long userId,@PathVariable Long companyId, @RequestHeader(value = "SESSION") String token )
    {
        if(authStore.sessionExists(token)==false)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Message","Token invalid try log in again").body(null);

        User user = userService.findByEmail(authStore.getUsername(token));
        if(user==null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Message","token invalid").body(null);

        Company company = companyService.findById(companyId);
        if(company==null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Message","Not a known company").body(null);

        if((long)company.getOwner()!=user.getId())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Message","Not authenticated as an Admin or Company User").body(null);

        Specialist specialist = Specialist.builder()
                .user(user)
                .company(company)
                .build();
        specialistService.signSpecialist(specialist);

        return ResponseEntity.ok(null);
    }

    //todo:: seach for a specialist base on user_id
    //todo:: search for a specialist base on company id
    //todo:: validate specialist (based on availability) - important: soon
    //todo:: create a specialist : done
    //todo:: add a specialist an account :
    //todo:: add some kind of regex search in finds  :
}
