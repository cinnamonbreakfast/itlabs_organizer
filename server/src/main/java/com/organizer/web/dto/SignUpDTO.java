package com.organizer.web.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
public class SignUpDTO extends BaseDTO {
    private String email;
    private String name;
    private String phone;
    private String city;
    private String country;
    private String password;
    private Integer code;
}
