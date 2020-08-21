package com.organizer.core.service;

import com.organizer.core.model.AnimeList;
import com.organizer.core.repository.AnimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnimeService {
    @Autowired
    AnimeRepository animeRepository;
    public AnimeService(AnimeRepository animeRepository){
        this.animeRepository= animeRepository;
    }
    public List<AnimeList> getAnimeList(int pageNumber ,String value){
        Pageable pageable = PageRequest.of(pageNumber,5);
        return this.animeRepository.getAnimeList(pageable,value).getContent();
    }
}
