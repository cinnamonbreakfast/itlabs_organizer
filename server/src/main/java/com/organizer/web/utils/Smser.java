package com.organizer.web.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class Smser {

    String URL = "https://app.smso.ro/api/v1/send";
    String API_KEY ="mTr3xmoP3M9usuncicnqdD57DbxHlXWTpz4uePpz";
    private final RestTemplate restTemplate;
    @Autowired
    public Smser(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String  sendSms(String receiver, String body){
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL);
        builder.queryParam("sender","4");
        builder.queryParam("to",receiver);
        builder.queryParam("body",body);
        builder.queryParam("apiKey",API_KEY);
        try {
            return restTemplate.getForEntity(builder.toUriString(), String.class).getBody();
        }
        catch (Exception e ){
            return null;
        }
    }
}
