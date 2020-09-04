package com.organizer.core.repository;


import com.organizer.core.model.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdminRepository extends  Repository<Long, Company>{

    @Query("select c from Company c where c.validated=false")
    Page<Company> findCompanies(Pageable pageable);


}
