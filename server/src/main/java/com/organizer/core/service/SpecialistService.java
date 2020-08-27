package com.organizer.core.service;


import com.organizer.core.model.Company;
import com.organizer.core.model.Specialist;
import com.organizer.core.model.User;
import com.organizer.core.repository.SpecialistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecialistService {
    SpecialistRepository specialistRepository;

    @Autowired
    public SpecialistService(SpecialistRepository specialistRepository){
        this.specialistRepository=specialistRepository;
    }
    public List<Specialist> findByCompanyUsername(String username) {
        return specialistRepository.findByUsername( username);
    }

    public List<Specialist> findByCompanyAndServiceName(String service,String username){
        return specialistRepository.findByCompanyAndServiceName(service,username);
    }

    public Specialist findByUser(User user)
    {
        return specialistRepository.findByUser(user);
    }
    public Specialist findById(Long id){
        return  specialistRepository.findById(id).get();
    }
    public void save (Specialist specialist ){
        specialistRepository.save(specialist);

    }
}