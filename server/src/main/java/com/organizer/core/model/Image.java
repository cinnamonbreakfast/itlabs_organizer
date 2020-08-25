package com.organizer.core.model;

import lombok.*;

import javax.persistence.*;
import javax.persistence.Entity;
import java.lang.annotation.Annotation;


@Table(name="Image",schema="public")
@javax.persistence.Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@org.hibernate.annotations.NamedQuery(name="Company.findByName", query = "select c from Company c where c.name = ?1")
@org.hibernate.annotations.NamedQuery(name="Company.findByCity", query = "select c from Company c where c.city = ?1")
@org.hibernate.annotations.NamedQuery(name="Company.findByCityAndCategory",query = "select c from Company c where c.city = ?1 and c.category = ?2")
public class Image extends com.organizer.core.model.Entity<Long> {

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "picByte",length = 1024)
    private byte[] picByte;

}
