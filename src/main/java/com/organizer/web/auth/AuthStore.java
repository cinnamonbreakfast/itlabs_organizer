package com.organizer.web.auth;

import javafx.util.Pair;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthStore {
    Map<String, Pair<String, LocalDateTime>> sessions;
    private final Integer EXPIRATION_TIME = 1; // days

    public AuthStore() {
        this.sessions = new HashMap<>();
    }

    public Pair<String, LocalDateTime> createToken(String username) {
        String token = RandomStringUtils.randomAlphabetic(20);
        LocalDateTime loginTime = LocalDateTime.now();

        sessions.put(token, new Pair<>(username, loginTime));

        return new Pair<String, LocalDateTime>(token, loginTime);
    }

    public boolean pop(String token)
    {
        sessions.remove(token);
        return true;
    }

    public String getUsername(String token) {
        return sessions.get(token).getKey();
    }

    public boolean sessionExists(String token) {
        LocalDateTime actualTime = LocalDateTime.now();

        if(sessions.containsKey(token))
        {
            if(sessions.get(token).getValue().until(actualTime, ChronoUnit.DAYS) > EXPIRATION_TIME)
            {
                sessions.remove(token);
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
}
