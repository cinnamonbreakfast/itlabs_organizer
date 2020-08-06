package com.organizer.web.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
public class SpecialistDTO extends BaseDTO{
    private String name;
    private String phone;
}
