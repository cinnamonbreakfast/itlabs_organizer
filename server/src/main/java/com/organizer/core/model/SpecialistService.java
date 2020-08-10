package com.organizer.core.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Table(name="specialistservice",schema="public")
@javax.persistence.Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
public class SpecialistService extends Entity<Long>{

    @Basic
    @Column
    private Float duration;

    @Basic
    @Column(name="service_name")
    private String serviceName;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="specialist_id", nullable = false)
    private Specialist specialist;

}
