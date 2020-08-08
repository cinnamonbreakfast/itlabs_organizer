package com.organizer.core.model;

import lombok.*;

import javax.persistence.*;

@Table(name="service",schema="public")
@javax.persistence.Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
public class Service extends Entity<Long>{
    @Basic
    @Column(name="service_name")
    private String name;

    @ManyToOne
    @JoinColumn(name="company_id",referencedColumnName = "id",nullable = false)
    private Company company;
}
