package com.organizer.core.repository;

import com.organizer.core.model.Specialist;

import java.util.List;

public interface SpecialistRepository extends Repository<Long, Specialist> {
    Specialist findByPhone(String phone);
    List<Specialist> findByCompany(String company);

}
