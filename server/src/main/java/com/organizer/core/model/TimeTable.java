package com.organizer.core.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Table(name="time_table",schema="public")
@javax.persistence.Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
public class TimeTable extends  Entity<Long>{

    @Basic
    @Column(name="start_time")
    private LocalDateTime start;

    @Basic
    @Column(name="end_time")
    private LocalDateTime end;

    @ManyToOne
    @JoinColumn(name="service")
    Service service;


}
