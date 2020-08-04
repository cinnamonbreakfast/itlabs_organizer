package com.organizer.web.utils;

import lombok.*;
import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Builder
@Getter
public class AuthSession {
    String username;
    LocalDateTime loginTime;

}
