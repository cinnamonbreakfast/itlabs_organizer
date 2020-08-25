package com.organizer.web.dto.schedulling;

import com.organizer.web.dto.BaseDTO;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(callSuper = true)
@Getter
@Builder
public class TimeTableDTO  {
    String companyUsername;
    String serviceName;
    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime start;

    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime end;


}
