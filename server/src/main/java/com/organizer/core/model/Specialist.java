package com.organizer.core.model;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name="specialist",schema="public")
@javax.persistence.Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NamedQuery(name = "Specialist.findByCompany", query = "select s from Specialist s where s.company.id= ?1")
@NamedQuery(name = "Specialist.findByUser", query= " select s from Specialist s where s.user.id=?1")

public class Specialist extends Entity<Long>{
    @ManyToOne
    @JoinColumn(name="company_id",referencedColumnName = "id",nullable = false)
    private Company company;

    @ManyToOne
    @JoinColumn(name="user_id",referencedColumnName = "id",nullable = false)
    private User user;
}
