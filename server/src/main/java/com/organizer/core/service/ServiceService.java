package com.organizer.core.service;

import com.organizer.core.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ServiceService {
    ServiceRepository serviceRepository;
    @Autowired
    public ServiceService(ServiceRepository serviceRepository){
        this.serviceRepository=serviceRepository;
    }

    public com.organizer.core.model.Service signService(com.organizer.core.model.Service service)
    {
        return serviceRepository.save(service);
    }

    public com.organizer.core.model.Service findById(Long id){
        return serviceRepository.findById(id).get();
    }

}
