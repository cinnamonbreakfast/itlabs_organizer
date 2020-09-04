package com.organizer.web.dto.schedulling;

import com.organizer.web.dto.ServiceDTO;
import com.organizer.web.dto.SpecialistDTO;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(callSuper = true)
@Getter
@Builder
public class ScheduleDTO {
    Long id;

    Long duration;
    Long specialistId;

    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime start;

    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime end;


    String s_start;
    String s_end;
    SpecialistDTO specialistDTO;
}
