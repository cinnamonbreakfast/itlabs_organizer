package com.organizer.core.model;

import lombok.*;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Table;

@Table(name="specialist",schema="production")
@javax.persistence.Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NamedQuery(name = "Specialist.findByPhone", query = "select s from Specialist s where s.phone = ?1")
@NamedQuery(name = "Specialist.findByCompany", query = "select s from Specialist s where s.companyId= ?1")

public class Specialist extends Entity<Long>{
    @Basic
    @Column(name="company_id")
    Integer companyId;

    @Basic
    @Column(name="user_id")
    Integer userId;

    @Basic
    @Column
    String name;

    @Basic
    @Column
    String phone;
}
