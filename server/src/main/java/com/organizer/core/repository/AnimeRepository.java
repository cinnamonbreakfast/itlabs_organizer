package com.organizer.core.repository;

import com.organizer.core.model.AnimeList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface AnimeRepository extends Repository<Long, AnimeList>{
    @Query("select a from AnimeList a where lower(a.list) like concat('%',lower(?1),'%') ")
    Page<AnimeList> getAnimeList(Pageable pageable,String value);


    @Query("select count(a) from AnimeList a where lower(a.list) like concat('%',lower(?1),'%') ")
    Long countByList(String list );

    @Query("select a from AnimeList a where lower(a.list) like concat('%',lower(?1),'%') ")
    AnimeList findByList(String list);

}
