package com.organizer.core.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.NamedQuery;

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
    @Column
    private Integer role;

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
    private Integer verifiedPhone;

    @Basic
    @Column(name = "verified_email")
    private Integer verifiedEmail;

    @JsonManagedReference
    @OneToMany(mappedBy = "user")
    private List<Schedule>schedules;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getVerifiedPhone() {
        return verifiedPhone;
    }

    public void setVerifiedPhone(Integer verifiedPhone) {
        this.verifiedPhone = verifiedPhone;
    }

    public Integer getVerifiedEmail() {
        return verifiedEmail;
    }

    public void setVerifiedEmail(Integer verifiedEmail) {
        this.verifiedEmail = verifiedEmail;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }
}
