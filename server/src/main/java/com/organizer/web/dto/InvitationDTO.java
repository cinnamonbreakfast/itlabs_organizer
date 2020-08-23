package com.organizer.web.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(callSuper = true)
@Builder

public class InvitationDTO {
    Long id;
    Boolean accept;
    String serviceName;
    CompanyDTO companyDTO;


}
