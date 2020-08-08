package com.organizer.core.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Table(name="timetable",schema="public")
@javax.persistence.Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
public class TimeTable extends  Entity<Long>{
    @Basic
    @Column
    private Date date;

    @Basic
    @Column(name="start_time")
    private Date startTime;

    @Basic
    @Column(name="end_time")
    private Date endTime;

}
