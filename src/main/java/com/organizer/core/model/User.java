package com.organizer.core.model;

import lombok.*;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Table;

@Table(name="users", schema="production")
@javax.persistence.Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NamedQuery(name = "User.findByUsernameAndPassword", query = "select u from User u where u.phone = ?1 and u.password = ?2")
@NamedQuery(name = "User.findByPhone", query = "select u from User u where u.phone = ?1")
public class User extends Entity<Long> {
    @Basic(optional = false)
    @Column(nullable = false)
    private String firstName;

    @Basic(optional = false)
    @Column(nullable = false)
    private String lastName;

    @Basic(optional = false)
    @Column(nullable = false)
    private String phone;

    @Basic(optional = false)
    @Column(nullable = false)
    private String city;

    @Basic(optional = false)
    @Column(nullable = false)
    private String country;

    @Basic(optional = false)
    @Column(nullable = false)
    private String password;

}
