package com.organizer.core.service;

import com.organizer.core.model.CategoryList;
import com.organizer.core.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository animeRepository;
    public CategoryService(CategoryRepository animeRepository){
        this.animeRepository= animeRepository;
    }
    public List<CategoryList> getAnimeList(int pageNumber , String value){
        Pageable pageable = PageRequest.of(pageNumber,5);
        return this.animeRepository.getAnimeList(pageable,value).getContent();
    }
    public Long getCount(String list){
        return animeRepository.countByList(list);
    }

    public CategoryList findByList(String list){
        return animeRepository.findByList(list);
    }
}
