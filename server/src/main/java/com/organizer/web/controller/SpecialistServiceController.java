package com.organizer.web.controller;

import com.organizer.core.model.SpecialistService;
import com.organizer.core.service.SpecialistServiceService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpecialistServiceController {
    SpecialistServiceService specialistServiceService;
    @Autowired
    public SpecialistServiceController(SpecialistServiceService specialistServiceService)
    {
        this.specialistServiceService = specialistServiceService;
    }
    @RequestMapping(value="ss/find/{id}")
    public ResponseEntity<SpecialistService> findSpecialistServiceById(@PathVariable Long id){
        SpecialistService specialistService = specialistServiceService.findById(id);
        return ResponseEntity.ok(specialistService);
    }
}
