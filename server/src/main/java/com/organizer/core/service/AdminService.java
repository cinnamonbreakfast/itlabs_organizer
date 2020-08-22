package com.organizer.core.service;

import com.organizer.core.model.Company;
import com.organizer.core.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private AdminRepository adminRepository;
    @Autowired
    public AdminService(AdminRepository adminRepository){
        this.adminRepository=adminRepository;
    }
    public List<Company> getCompanies(int page){
        Pageable pageable = PageRequest.of(page ,10);

        return adminRepository.findCompanies(pageable).getContent();
    }
}
