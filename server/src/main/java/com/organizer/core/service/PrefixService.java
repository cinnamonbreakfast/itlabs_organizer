package com.organizer.core.service;

import com.organizer.core.model.Prefix;
import com.organizer.core.repository.PrefixRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrefixService {

    PrefixRepository prefixRepository;
    @Autowired
    public  PrefixService(PrefixRepository prefixRepository){
        this.prefixRepository =prefixRepository;
    }

    public List<Prefix> findByCountryOrPrefix() {

        return prefixRepository.findPrefixByCountryOrPrefix();
    }
    public Prefix findByPrefix(String prefix){
        return prefixRepository.findPrefixByPrefix(prefix);

    }
}
