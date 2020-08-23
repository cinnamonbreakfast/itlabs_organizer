package com.organizer.core.service;


import com.organizer.core.model.Specialist;
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
    public Specialist findById(Long user_id){ return specialistRepository.findByUser(user_id);}
    public List<Specialist> findByCompany(Long company_id){
        return specialistRepository.findByCompany(company_id);
    }
    public Specialist signSpecialist(Specialist specialist)
    {
        return specialistRepository.save(specialist);
    }

    public List<Specialist> findByCompanyTest(){
        return specialistRepository.findByCompanyTest();
    }
    public List<Specialist> findByCompanyUsername(String username) {
        return specialistRepository.findByUsername( username);
    }

    public List<Specialist> findByCompanyAndServiceName(String service,String username){
        return specialistRepository.findByCompanyAndServiceName(service,username);
    }

    public void save (Specialist specialist ){
        specialistRepository.save(specialist);
    }
}