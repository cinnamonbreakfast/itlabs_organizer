package com.organizer.web.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
public class UserDTO extends BaseDTO {
    private String email;
    private String name;
    private String phone;
    private Integer role;
    private String imageURL;
    private String city;
    private String country;
}
