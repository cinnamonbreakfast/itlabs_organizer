package com.organizer.core.model;

import lombok.*;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Table;

@Table(name="countrylist",schema="public")
@javax.persistence.Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Builder
@Data
@EqualsAndHashCode(callSuper = true)
public class CountryList  extends  Entity<Long>
{
    @Basic
    @Column
    String country;

    @Basic
    @Column
    String abbreviation;

}
