package com.organizer.web.utils;

import com.organizer.core.model.Company;
import com.organizer.core.model.Specialist;
import com.organizer.core.model.SpecialistService;
import com.organizer.core.model.User;
import com.organizer.web.dto.SpecialistDTO;
import com.organizer.web.dto.UserDTO;

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
    public static List<SpecialistDTO> getSpecialisDTOtFromCompany(Company company){
        List<SpecialistDTO> specialistDTOS = new ArrayList<>(company.getSpecialists().size());
        for(Specialist specialist : company.getSpecialists()){
            User user = specialist.getUser();
            UserDTO userDTO = UserDTO.builder()
                    .email(user.getEmail())
                    .phone(user.getPhone())
                    .name(user.getName())
                    .country(user.getCountry())
                    .city(user.getCity())
                    .imageURL(user.getImageURL())
                    .build();
            userDTO.setId(user.getId());

            SpecialistDTO specialistDTO = SpecialistDTO.builder()

                    .user(userDTO)
                    .build();
            specialistDTO.setId(specialist.getId());
            specialistDTOS.add(specialistDTO);
        }
        return specialistDTOS;
    }
}
