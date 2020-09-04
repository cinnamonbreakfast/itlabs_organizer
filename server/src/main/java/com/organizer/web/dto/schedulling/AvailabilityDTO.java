package com.organizer.web.dto.schedulling;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@ToString(callSuper = true)
@Getter
@Builder
@AllArgsConstructor
public class AvailabilityDTO {
    Long id;
    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime start;

    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime end;
    String reason;
}

