package com.organizer.core.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.*;
import java.util.List;

@Table(name="Company",schema="public")
@javax.persistence.Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NamedQuery(name="Company.findByName", query = "select c from Company c where c.name = ?1")
@NamedQuery(name="Company.findByCity", query = "select c from Company c where c.city = ?1")
@NamedQuery(name="Company.findByCityAndCategory",query = "select c from Company c where c.city = ?1 and c.category = ?2")
public class Company extends Entity<Long>{


    @Basic(optional = false)
    @Column(nullable = false)
    private String city;

    @Basic(optional = false)
    @Column(nullable = false)
    private String name;

    @Basic
    @Column
    private String category;

    @Basic(optional = false)
    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    @Basic(optional = false)
    private String country;

    @Basic
    @Column(name="username")
    private String username;

    @OneToOne
    @JoinColumn(name = "owner")
    private User owner;

    @JsonManagedReference
    @OneToMany(mappedBy = "company")
    private List<Specialist> specialists;

    @Basic
    @Column
    String image_url;


}
