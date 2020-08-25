package com.organizer.core.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Table(name="service",schema="public")
@javax.persistence.Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
public class Service extends Entity<Long>{

    @Basic
    @Column
    private Float duration;

    @Basic
    @Column(name="service_name")
    private String serviceName;

    @Basic
    @Column
    private Float price;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="company", nullable = false)
    private Company company;

    @JsonManagedReference
    @OneToMany(mappedBy = "service")
    private List<TimeTable> timeTables;

    @JsonManagedReference
    @OneToMany(mappedBy = "service")
    private List<Availability> availabilities;

    @JsonManagedReference
    @OneToMany(mappedBy = "service")
    private List<Specialist> specialists;


}
