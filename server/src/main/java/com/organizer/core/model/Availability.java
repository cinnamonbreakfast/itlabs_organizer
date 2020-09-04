package com.organizer.core.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.NamedQuery;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Date;

@Table(name="availability",schema="public")
@javax.persistence.Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
public class Availability extends Entity<Long>{
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name="service", nullable = false)
    private Service service;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name="specialist", nullable = false)
    Specialist specialist;

    @Basic
    @Column(name="start_time")
    private LocalDateTime start;

    @Basic
    @Column(name="end_time")
    private LocalDateTime end;

    @Basic
    @Column
    private String reason;
}
