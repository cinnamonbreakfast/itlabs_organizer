package com.organizer.core.repository;

import com.organizer.core.model.AdminValidation;
import com.organizer.core.model.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdminRepository extends  Repository<Long, AdminValidation>{

    @Query("select a.company from AdminValidation a where a.validated=false ")
    Page<Company> findCompanies(Pageable pageable);


}
