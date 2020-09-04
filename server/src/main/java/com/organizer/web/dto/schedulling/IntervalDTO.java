package com.organizer.web.dto.schedulling;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
@NoArgsConstructor
@Data
@ToString(callSuper = true)
@Getter
@Builder
@AllArgsConstructor
public class IntervalDTO {

    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime date;

    Long specialist_id;


    String start;

    String end;
}
