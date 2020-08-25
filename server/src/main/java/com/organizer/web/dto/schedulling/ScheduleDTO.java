package com.organizer.web.dto.schedulling;

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
}
