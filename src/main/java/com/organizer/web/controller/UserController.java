package com.organizer.web.controller;

import com.organizer.core.model.User;
import com.organizer.core.service.UserService;
import com.organizer.web.auth.AuthStore;
import com.organizer.web.dto.UserDTO;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
public class UserController {
    private final UserService userService;
    private final AuthStore authStore;

    @Autowired
    public UserController(UserService userService, AuthStore authStore) {
        this.userService = userService;
        this.authStore = authStore;
    }

    @RequestMapping(value = "u/auth", method = RequestMethod.POST)
    public ResponseEntity<UserDTO> authenticate(@RequestBody(required = true) String email, @RequestBody(required = true) String password) {
        User authUser = userService.emailAuth(email, password);
        ResponseEntity<UserDTO> response;

        if(authUser != null) {
            // create a token and return it
            Pair<String, LocalDateTime> oauthPair = authStore.createToken(authUser.getId().toString());

            response = new ResponseEntity<>(new UserDTO(), HttpStatus.UNAUTHORIZED);
//            response.getHeaders().add("TOKEN", oauthPair.getKey());
//            response.getHeaders().add("AUTH_TIME", oauthPair.getValue().toString());
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("TOKEN", oauthPair.getKey());
            responseHeaders.add("AUTH_TIME", oauthPair.getValue().toString());

            UserDTO authResponseUser = UserDTO.builder()
                    .email(authUser.getEmail())
                    .name(authUser.getName())
                    .phone(authUser.getPhone())
                    .role(authUser.getRole())
                    .imageURL(authUser.getImageURL())
                    .city(authUser.getCity())
                    .country(authUser.getCountry()).build();

            return ResponseEntity.ok()
                    .headers(responseHeaders)
                    .body(authResponseUser);
        }

        // bad credentials, unauthorized request + message
        response = new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("MESSAGE", "Wrong E-mail address or password.");
        return response;
    }

    @RequestMapping(value = "u/signup", method = RequestMethod.POST)
    public ResponseEntity<String> signUp(@RequestBody(required = true) String email, @RequestBody(required = true) String password) {
        User existingUser = userService.findByEmail(email);

        if(existingUser == null) {
            // do sign up
            User user = User.builder()
                    .email(email)
                    .password(password)
                    .build();

            if(userService.signUpEmailAndPassword(user) != null) {
                return new ResponseEntity<>("Registration complete. You can sign in now.", HttpStatus.OK);
            }

            return new ResponseEntity<>("There was a problem with your registration. Try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("This E-mail address is already used.", HttpStatus.FORBIDDEN);
    }
}
