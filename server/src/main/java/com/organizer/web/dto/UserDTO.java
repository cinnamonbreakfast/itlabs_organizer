package com.organizer.web.dto;

import com.organizer.core.model.Invitation;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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
    private String imageURL;
    private String city;
    private String country;
    private String token;
    private String authTime;
    private Boolean verifiedEmail;
    private Boolean verifiedPhone;
    List<InvitationDTO> invitationDTOS;
}
