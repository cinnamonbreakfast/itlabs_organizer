package com.organizer.core.repository;

import com.organizer.core.model.CategoryList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends Repository<Long, CategoryList>{
    @Query("select a from CategoryList a where lower(a.list) like concat('%',lower(?1),'%') ")
    Page<CategoryList> getAnimeList(Pageable pageable, String value);


    @Query("select count(a) from CategoryList a where lower(a.list) like concat('%',lower(?1),'%') ")
    Long countByList(String list );

    @Query("select a from CategoryList a where lower(a.list) like concat('%',lower(?1),'%') ")
    CategoryList findByList(String list);

}
