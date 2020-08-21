package com.organizer.web.dto;

import com.organizer.core.model.Company;
import com.organizer.core.model.Specialist;
import com.organizer.core.model.User;
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
public class SpecialistDTO extends BaseDTO {
   private ServiceDTO serviceDTO;
   private UserDTO user;
   private CompanyDTO company;
}
