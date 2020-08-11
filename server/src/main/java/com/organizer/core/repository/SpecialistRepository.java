package com.organizer.core.repository;

import com.organizer.core.model.Specialist;

import java.util.List;

public interface SpecialistRepository extends Repository<Long, Specialist>{
    List<Specialist> findByCompany(Long company);
    Specialist findByUser(Long user);

}
