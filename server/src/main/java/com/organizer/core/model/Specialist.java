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
@NamedQuery(name = "Specialist.findByCompany", query = "select s from Specialist s where s.company.id = ?1")
@NamedQuery(name = "Specialist.findByUser", query= " select s from Specialist s where s.user.id = ?1")
public class Specialist extends Entity<Long>{
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="company_id")
    private Company company;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @JsonManagedReference
    @OneToMany(mappedBy = "specialist")
    private List<SpecialistService> specialistServices ;

    @JsonManagedReference
    @OneToMany(mappedBy = "specialist")
    private List<TimeTable> timeTables;


    @JsonManagedReference
    @OneToMany(mappedBy = "specialist")
    private List<Schedule> schedules;

    @JsonManagedReference
    @OneToMany(mappedBy = "specialist")
    private List<Availability> availabilities;

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<SpecialistService> getSpecialistServices() {
        return specialistServices;
    }

    public void setSpecialistServices(List<SpecialistService> specialistServices) {
        this.specialistServices = specialistServices;
    }

    public List<TimeTable> getTimeTables() {
        return timeTables;
    }

    public void setTimeTables(List<TimeTable> timeTables) {
        this.timeTables = timeTables;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }

    public List<Availability> getAvailabilities() {
        return availabilities;
    }

    public void setAvailabilities(List<Availability> availabilities) {
        this.availabilities = availabilities;
    }
}
