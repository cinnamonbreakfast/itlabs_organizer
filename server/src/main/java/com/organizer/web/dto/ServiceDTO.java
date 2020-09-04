package com.organizer.web.dto;

import com.organizer.core.model.TimeTable;
import com.organizer.web.dto.schedulling.TimeTableDTO;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Builder
@Setter

public class ServiceDTO extends  BaseDTO{
    String companyUsername;
    String name;
    Float duration;
    Float price;
    TimeTableDTO timeTableDTO;
    CompanyDTO companyDTO;
    List<SpecialistDTO> specialistDTOList;
}
