package com.organizer.core.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Table(name="schedule",schema="public")
@javax.persistence.Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
public class Schedule extends Entity<Long>{
    @Basic
    @Column(name="start_date")
    Date startDate;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="specialist_id", nullable = false)
    private Specialist specialist;


    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;




}
