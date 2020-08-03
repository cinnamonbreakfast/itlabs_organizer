package com.organizer.core.repository;

import com.organizer.core.model.Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface Repository<ID extends Serializable, T extends Entity<ID>> extends JpaRepository<T, ID> {

}
