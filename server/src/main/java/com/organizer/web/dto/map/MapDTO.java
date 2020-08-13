package com.organizer.web.dto.map;

import com.organizer.web.dto.CompanyDTO;
import com.organizer.web.dto.ServiceDTO;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class MapDTO {
    CompanyDTO company;
    ServiceDTO serviceDTO;
}
