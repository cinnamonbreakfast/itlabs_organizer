package com.organizer.core.model;

import lombok.*;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

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
@NamedQuery(name = "User.findByEmail", query = "select u from User u where u.email = ?1")

public class User extends Entity<Long> {
    @Basic(optional = false)
    @Column(nullable = false)
    private String email;

    @Basic
    @Column
    private String name;

    @Basic
    @Column
    private String phone;

    @Basic
    @Column
    private Integer role;

    @Basic
    @Column(name = "image_url")
    private String imageURL;

    @Basic(optional = false)
    @Column(nullable = false)
    private String password;

    @Basic
    @Column
    private String city;

    @Basic
    @Column
    private String country;

    @Basic
    @Column(name = "verified_phone")
    private Integer verifiedPhone;

    @Basic
    @Column(name = "verified_email")
    private Integer verifiedEmail;

}
