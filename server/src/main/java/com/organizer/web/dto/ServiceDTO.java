package com.organizer.web.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Builder
@Setter

public class ServiceDTO extends  BaseDTO{
    CompanyDTO companyDTO;
    String name;
}
