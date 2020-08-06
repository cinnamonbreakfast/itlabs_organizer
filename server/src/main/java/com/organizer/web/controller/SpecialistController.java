package com.organizer.web.controller;

import com.organizer.core.model.Specialist;
import com.organizer.core.service.SpecialistService;
import com.organizer.web.dto.SpecialistDTO;
import com.organizer.web.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SpecialistController {
    SpecialistService specialistService;
    @Autowired
    public SpecialistController(SpecialistService specialistService){
        this.specialistService = specialistService;
    }
    @RequestMapping(value = "/s/search/by/phone/{phone}", method = RequestMethod.GET)
    public ResponseEntity<SpecialistDTO> findOne(@PathVariable(required= true)String phone){
        //todo::validate token
        Specialist specialist = specialistService.findByPhone(phone);
        if(specialist!=null){
            SpecialistDTO specialistDTO = SpecialistDTO.builder()
                    .name(specialist.getName())
                    .phone(specialist.getPhone());
            return ResponseEntity.ok(specialistDTO);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
    @RequestMapping(value = "/s/search/by/company/{company_id}", method = RequestMethod.GET)
    public ResponseEntity<List<SpecialistDTO>> findOne(@PathVariable(required= true)String company_id){
        //todo::validate token
        List<Specialist> specialists = specialistService.findByCompany(company_id);

        if(specialists!=null){
            List<SpecialistDTO> specialistDTOS = new ArrayList<>(specialists.size());
            for(Specialist specialist : specialists) {
                SpecialistDTO specialistDTO = SpecialistDTO.builder()
                        .name(specialist.getName())
                        .phone(specialist.getPhone());
                specialistDTOS.add(specialistDTO);
            }
            return ResponseEntity.ok(specialistDTOS);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
    //todo::search for a single specialist from a company : done
    //todo:: list of specialist from a company : : done
    //todo:: validate specialist (based on availability) : 
    //todo:: create a specialist
    //todo:: add a specialist an account
    //todo:: edit
    //todo ::
}
