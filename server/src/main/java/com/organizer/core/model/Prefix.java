package com.organizer.core.model;

import lombok.*;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Table;

@Table(name="phone_codes",schema="public")
@javax.persistence.Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
public class Prefix  extends Entity<Long>{
    @Basic
    @Column
    String country;

    @Basic
    @Column
    String prefix;
}
