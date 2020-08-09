package com.organizer.core.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;

@Table(name="service",schema="public")
@javax.persistence.Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@org.hibernate.annotations.NamedQuery(name = "Service.findBySpecialist", query= " select se from Service se where se.specialist.id = ?1")
public class Service extends Entity<Long>{
    @Basic
    @Column(name="service_name")
    private String name;

    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="specialist_id", nullable = false)
    private Specialist specialist;
}
