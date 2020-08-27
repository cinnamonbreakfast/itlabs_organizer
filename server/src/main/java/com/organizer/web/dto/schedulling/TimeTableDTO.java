package com.organizer.web.dto.schedulling;

import com.organizer.web.dto.BaseDTO;
import com.organizer.web.dto.ServiceDTO;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(callSuper = true)
@Getter
@Builder
public class TimeTableDTO  extends BaseDTO {
    Long id;
    String companyUsername;
    String serviceName;
    Integer day;
    @DateTimeFormat(iso=DateTimeFormat.ISO.TIME)
    LocalTime start;

    @DateTimeFormat(iso=DateTimeFormat.ISO.TIME)
    LocalTime end;

    String s_end;
    String s_start;

    ServiceDTO serviceDTO;

}
