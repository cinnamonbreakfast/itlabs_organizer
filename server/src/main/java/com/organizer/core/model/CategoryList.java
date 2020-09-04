package com.organizer.core.model;


import com.organizer.web.dto.BaseDTO;
import lombok.*;

import javax.persistence.*;



@Table(name="categorylist",schema="public")
@javax.persistence.Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Builder
@Data
@EqualsAndHashCode(callSuper = true)
public class CategoryList extends Entity<Long> {
    @Basic
    @Column(name="list")
    String list;
}
