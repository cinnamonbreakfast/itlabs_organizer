package com.organizer.core.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

@Table(name="admin_validation",schema="public")
@javax.persistence.Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Builder
@Data
@EqualsAndHashCode(callSuper = true)
public class AdminValidation extends Entity<Long>{

    @JsonBackReference
    @OneToOne
    @JoinColumn(name="company", nullable = false)
    Company company;

    @Basic
    @Column
    Boolean validated;
}
