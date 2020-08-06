package com.organizer.core.model;


import lombok.*;

import javax.persistence.Basic;
import javax.persistence.Column;
import org.hibernate.annotations.NamedQuery;
import javax.persistence.Table;

@Table(name="company",schema="public")
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
//@NamedQuery(name="Company.findByCityAndCountry",query = "select c from Company c where c.city")


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



}
