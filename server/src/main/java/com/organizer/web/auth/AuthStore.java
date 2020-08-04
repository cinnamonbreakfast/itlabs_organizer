package com.organizer.web.auth;

import com.organizer.web.utils.AuthSession;
import javafx.util.Pair;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthStore {
    Map<String, AuthSession> sessions;
    private final Integer EXPIRATION_TIME = 1; // days

    public AuthStore() {
        this.sessions = new HashMap<>();
    }

    public AuthSession createToken(String username) {
        String token = RandomStringUtils.randomAlphabetic(20);
        LocalDateTime loginTime = LocalDateTime.now();


        AuthSession localSession = AuthSession.builder()
                .token(username)
                .loginTime(loginTime)
                .build();


        sessions.put(token, localSession);

        return localSession;
    }

    public boolean pop(String token)
    {
        sessions.remove(token);
        return true;
    }

    public String getUsername(String token) {
        return sessions.get(token).getToken();
    }

    public boolean sessionExists(String token) {
        LocalDateTime actualTime = LocalDateTime.now();

        if(sessions.containsKey(token))
        {
            if(sessions.get(token).getLoginTime().until(actualTime, ChronoUnit.DAYS) > EXPIRATION_TIME)
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
