package com.organizer.core.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

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
