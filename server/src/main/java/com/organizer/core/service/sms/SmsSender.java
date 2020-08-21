package com.organizer.core.service.sms;

import static org.toilelibre.libe.curl.Curl.curl;
import static org.toilelibre.libe.curl.Curl.$;

import org.apache.http.HttpResponse;
import org.springframework.stereotype.Service;

@Service
public class SmsSender {

    String url = "https://app.smso.ro/api/v1/";
    String API_KEY ="mTr3xmoP3M9usuncicnqdD57DbxHlXWTpz4uePpz";
    public void sendSms(String sender){
        String url = "https://app.smso.ro/api/v1/";
        HttpResponse response = curl("curl -H \"X-Authorization: "+API_KEY+"\"  \"https://app.smso.ro/api/v1/\" ");
                //curl -H "X-Authorization: API-KEY"  "https://app.smso.ro/api/v1/"
        System.out.println(response);
    }
}
