package com.organizer.core.model;

import lombok.*;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Table;

@Table(name="citylist",schema="public")
@javax.persistence.Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Builder
@Data
@EqualsAndHashCode(callSuper = true)
public class CityList extends Entity<Long>{
    @Basic
    @Column
    String city;

    @Basic
    @Column
    String country;
}
