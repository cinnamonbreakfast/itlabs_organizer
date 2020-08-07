package com.organizer.web.dto;

import com.organizer.core.model.Specialist;
import com.organizer.core.model.User;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Builder
@Setter
public class SpecialistDTO extends BaseDTO {
    private String specialistEmail;
    private String specialistName;
    private String specialistPhone;
    private String specialistImageURL;
    private String specialistCity;
    private String specialistCountry;
    private String companyName;
    private String companyCity;
    private String companyAddress;
    private String companyCategory;
    private String companyCountry;
    private Long companyId;
    private Long userId;
}
