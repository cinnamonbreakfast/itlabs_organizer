package com.organizer.web.utils;

import com.organizer.core.model.Company;
import com.organizer.core.model.Specialist;
import com.organizer.core.model.SpecialistService;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Parser {
    public static List<SpecialistService> getServicesFromCompany(Company company)
    {
        Set<SpecialistService> specialistServiceSet = new LinkedHashSet<>();
        for(Specialist specialist : company.getSpecialists()){
            specialist.getSpecialistServices().stream().forEach(e->{specialistServiceSet.add(e);});
        }
        return new ArrayList<SpecialistService>(specialistServiceSet);
    }
}
