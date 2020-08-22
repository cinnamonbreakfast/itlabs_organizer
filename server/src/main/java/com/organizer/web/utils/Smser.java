package com.organizer.web.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@PropertySource("classpath:api.properties")
public class Smser {

    @Value("${sms.service.url}")
    String URL;

    @Value("${sms.service.api_key}")
    String API_KEY;

    @Value("${sms.service.sender}")
    String SENDER;

    private final RestTemplate restTemplate;
    @Autowired
    public Smser(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String  sendSms(String receiver, String body){
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL);
        builder.queryParam("sender",SENDER);
        builder.queryParam("to", receiver);
        builder.queryParam("body", body);
        builder.queryParam("apiKey",API_KEY);
        try {
            return restTemplate.getForEntity(builder.toUriString(), String.class).getBody();
        }
        catch (Exception e ){
            return null;
        }
    }
}
