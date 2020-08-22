package com.organizer.core.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.NamedQuery;
import org.springframework.stereotype.Component;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Table(name="users", schema="public")
@javax.persistence.Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NamedQuery(name = "User.findByEmailAndPassword", query = "select u from User u where u.email = ?1 and u.password = ?2")
@NamedQuery(name = "User.findByPhone", query = "select u from User u where u.phone = ?1")
public class User extends Entity<Long> {
    @Basic(optional = true)
    @Column(nullable = true)
    private String email;

    @Basic
    @Column
    private String name;

    @Basic(optional = false)
    @Column(nullable = false)
    private String phone;

    @Basic
    @Column(name = "image_url")
    private String imageURL;

    @Basic
    @Column
    private String password;

    @Basic
    @Column
    private String city;

    @Basic
    @Column
    private String country;

    @Basic
    @Column(name = "verified_phone")
    private Boolean verifiedPhone;

    @Basic
    @Column(name = "verified_email")
    private Boolean verifiedEmail;

    @Basic
    @Column
    private Boolean admin;

    @JsonManagedReference
    @OneToMany(mappedBy = "user")
    private List<Schedule>schedules;
}
