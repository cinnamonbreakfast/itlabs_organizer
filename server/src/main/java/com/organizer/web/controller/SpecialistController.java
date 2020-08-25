package com.organizer.web.controller;

import com.organizer.core.model.*;
import com.organizer.core.service.*;
import com.organizer.web.auth.AuthStore;
import com.organizer.web.auth.JWToken;
import com.organizer.web.dto.*;
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
    private final InvitationService invitationService;
    private final ServiceService serviceService;

    @Autowired
    public SpecialistController(SpecialistService specialistService, AuthStore authStore, UserService userService, CompanyService companyService, ServiceService serviceService, InvitationService invitationService){
        this.userService = userService;
        this.specialistService = specialistService;
        this.authStore= authStore;
        this.companyService= companyService;
        this.invitationService=invitationService;
        this.serviceService =serviceService;
    }

    @RequestMapping(value = "/specialist/search/{id}", method = RequestMethod.GET)
    public ResponseEntity<Specialist> findSomeSpecialist(@PathVariable Long id) {

        return new ResponseEntity<>(specialistService.findById(id), HttpStatus.OK);
    }


    @RequestMapping(value = "/s/search/by/user/{user_id}", method = RequestMethod.GET)
    public ResponseEntity<Specialist> findSpecialistByPhone(@PathVariable(required= true)Long user_id){


        Specialist specialists = specialistService.findById(user_id);
        return ResponseEntity.ok(specialists);
    }
    @RequestMapping(value = "/s/search/by/company/{username}", method = RequestMethod.GET)
    public ResponseEntity<List<SpecialistDTO>> findSpecialistsbyCompany(@PathVariable(required= true)String username){

        List<Specialist> specialists = specialistService.findByCompanyUsername(username);
        if(specialists!=null) {
            List<SpecialistDTO> specialistDTOS = new ArrayList<>(specialists.size());
            for (Specialist specialist : specialists) {
                User user = specialist.getUser();
                Company company = specialist.getService().getCompany();
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

    //company  and service
    @RequestMapping(value = "/s/find", method = RequestMethod.GET)
    public ResponseEntity <List<SpecialistDTO>> findSpecialistByCompany(@RequestParam String username,@RequestParam String serviceName){
        Company company = companyService.findByUsername(username);
        if(company== null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        List<Specialist> specialists =specialistService.findByCompanyAndServiceName(serviceName,username);

        List<SpecialistDTO> specialistDTOS = new ArrayList<>(specialists.size());

        for(Specialist specialist : specialists){

            Service specialistServices = specialist.getService();

            ServiceDTO serviceDTO = ServiceDTO.builder()
                    .duration(specialistServices.getDuration())
                    .price(specialistServices.getPrice())
                    .name(specialistServices.getServiceName())
                    .build();
            serviceDTO.setId(specialistServices.getId());
            User user = specialist.getUser();
            UserDTO userDTO = UserDTO.builder()
                    .imageURL(user.getImageURL())
                    .city(user.getCity())
                    .country(user.getCountry())
                    .name(user.getName())
                    .phone(user.getPhone())
                    .email(user.getEmail())
                    .build();
            userDTO.setId(user.getId());

            SpecialistDTO specialistDTO = SpecialistDTO.builder()
                    .servicesDTO(serviceDTO)
                    .user(userDTO)
                    .build();
            specialistDTO.setId(specialist.getId());
            specialistDTOS.add(specialistDTO);
        }
        return ResponseEntity.ok(specialistDTOS);
    }

    //display invitations
    @RequestMapping(value = "s/invitations",method = RequestMethod.GET)
    public ResponseEntity <List<InvitationDTO>> invitationsDisplay(@RequestHeader String token){
        Long id = JWToken.checkToken(token);
        if(id==null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        User user = userService.findById(id);
        if(user==null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        List <Invitation> invitations = invitationService.findByUserAndAccepted(user);
        List<InvitationDTO> invitationDTOS = new ArrayList<>(invitations.size());
        invitations.forEach(e->{
            invitationDTOS.add(
                InvitationDTO.builder()
                        .serviceDTO(
                                ServiceDTO.builder()
                                .name(e.getService().getServiceName())
                                .duration(e.getService().getDuration())
                                .price(e.getService().getPrice())
                                .build()
                        )
                        .id(e.getId())
                        .companyDTO(
                                CompanyDTO.builder()
                                        .username(e.getCompany().getUsername())
                                        .city(e.getCompany().getImage_url())
                                        .address(e.getCompany().getAddress())
                                        .city(e.getCompany().getCity())
                                        .category(e.getCompany().getCategory())
                                        .image_url(e.getCompany().getImage_url())
                                        .build()).build() );

        }
            );


        return  ResponseEntity.ok(invitationDTOS);

    }


    @RequestMapping( value = "s/acceptInvitation", method = RequestMethod.POST)
    public ResponseEntity<String> acceptInvitation(@RequestParam Long invitationId , @RequestHeader String token)
    {
        Long authId = JWToken.checkToken(token);
        if(authId==null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not a valid token");
        }
        User user = userService.findById(authId);
        if(user ==null)
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not a known user");
        }
        Invitation invitation = null;
        try {
             invitation = invitationService.findById(invitationId);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not a good invitation");
        }
        if(invitation==null)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Server error");
        }


        User invitedUser = invitation.getUser();
        if(invitedUser.getId()!=user.getId()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not a matching user");
        }

        Service service = invitation.getService();
        Invitation test = invitationService.findByUserAndServiceAndAccepted(user,service,true);
        if(test!=null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You have already accepted");
        }
        Specialist specialist = Specialist.builder()
                .service(service)
                .user(invitedUser)
                .build();

        invitation.setAccepted(true);

        try {
            specialistService.save(specialist);
            invitationService.save(invitation);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Accepted the invite");
        }

        catch (Exception e )
        {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something happened");
        }


    }




}
