package com.organizer.core.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "validation_codes", schema = "public")
@javax.persistence.Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NamedQuery(name = "ValidationCode.findByCode", query = "select c from ValidationCode c where c.code = ?1")
public class ValidationCode extends Entity<Long> {
    @OneToOne
    @JoinColumn(name = "account")
    private User account;

    @Basic
    @Column(nullable = false)
    private String purpose;

    @Basic
    @Column(nullable = false)
    private Integer code;

    @Basic
    @Column(nullable = false)
    private LocalDateTime dueDate;
}
