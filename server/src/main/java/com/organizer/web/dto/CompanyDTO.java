package com.organizer.web.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Builder
public class CompanyDTO extends BaseDTO {
        private String name;
        private String city;
        private String address;
        private String category;
        private String country;
        private String image_url;
        private String username;
        private UserDTO owner;
        private List<SpecialistDTO> staffMembers;
        private List<ServiceDTO> services;
}
