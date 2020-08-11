package com.organizer.core.service;

import com.organizer.core.model.SpecialistService;
import com.organizer.core.repository.SpecialistServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.env.SystemEnvironmentPropertySourceEnvironmentPostProcessor;
import org.springframework.stereotype.Service;

@Service
public class SpecialistServiceService {
    private SpecialistServiceRepository specialistServiceRepository;
    @Autowired
    public SpecialistServiceService(SpecialistServiceRepository specialistServiceRepository){
        this.specialistServiceRepository = specialistServiceRepository;
    }
    public SpecialistService findById(Long id){
        return specialistServiceRepository.findById(id).get();
    }

}
