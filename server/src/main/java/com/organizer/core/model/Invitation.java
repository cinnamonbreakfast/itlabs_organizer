package com.organizer.core.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

@Table(name="invitations",schema="public")
@javax.persistence.Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Builder
@Data
@EqualsAndHashCode(callSuper = true)

public class Invitation extends Entity<Long>{
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="company", nullable = false)
    Company company;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    User user ;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="service", nullable = false)
    Service service;

    @Basic
    @Column
    Boolean accepted;

}
