package com.organizer.core.service;

import com.organizer.core.model.Service;
import com.organizer.core.model.User;
import com.organizer.core.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@org.springframework.stereotype.Service
public class ServiceService {
    private ServiceRepository specialistServiceRepository;
    @Autowired
    public ServiceService(ServiceRepository specialistServiceRepository){
        this.specialistServiceRepository = specialistServiceRepository;
    }
    public Service findById(Long id){
        return specialistServiceRepository.findById(id).get();
    }

    public void save(Service specialistService){
        specialistServiceRepository.save(specialistService);
    }


    public Service findByServiceAndCompany(String service, String companyUsername){
        return specialistServiceRepository.findServiceByCompanyAndName(service,companyUsername);
    }
}
