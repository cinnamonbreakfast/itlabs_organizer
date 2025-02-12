package com.organizer.core.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.*;
import java.util.List;

@Table(name="specialist",schema="public")
@javax.persistence.Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder

@NamedQuery(name = "Specialist.findByUser", query= " select s from Specialist s where s.user.id = ?1")
public class Specialist extends Entity<Long>{
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="service")
    private Service service;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @JsonManagedReference
    @OneToMany(mappedBy = "specialist")
    private List<Schedule> schedules;

    @JsonManagedReference
    @OneToMany(mappedBy = "specialist")
    private List<Availability> availabilities;

}
