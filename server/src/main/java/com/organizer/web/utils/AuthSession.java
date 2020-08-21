package com.organizer.web.utils;

import lombok.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Builder
@Getter
public class AuthSession {
    String token;
    LocalDateTime loginTime;

    @Service
    public static class SmsSender {

        String url = "https://app.smso.ro/api/v1/";
        String API_KEY ="mTr3xmoP3M9usuncicnqdD57DbxHlXWTpz4uePpz";
        public void sendSms(String sender,String body){


            //https://app.smso.ro/api/v1/send?sender=4&to=0722334455&body=Message%20Body&apiKey=mTr3xmoP3M9usuncicnqdD57DbxHlXWTpz4uePpz

        }
    }
}
