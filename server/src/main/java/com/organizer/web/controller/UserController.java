package com.organizer.web.controller;

import com.organizer.core.model.User;
import com.organizer.core.service.UserService;
import com.organizer.web.auth.AuthStore;
import com.organizer.web.dto.UserDTO;
import com.organizer.web.utils.AuthSession;
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

        if(authUser != null) {
            // create a token and return it
            AuthSession oauthPair = authStore.createToken(authUser.getId().toString());

            // headers set
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("TOKEN", oauthPair.getToken());
            responseHeaders.add("AUTH_TIME", oauthPair.getLoginTime().toString());

            // DTO set
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

        // bad credentials
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).header("MESSAGE", "Wrong username or password.").body(null);
    }

    @RequestMapping(value = "u/signup", method = RequestMethod.POST)
    public ResponseEntity<String> signUp(@RequestParam(required = true) String email, @RequestParam(required = true) String password) {
        User existingUser = userService.findByEmail(email);

        if(existingUser == null) {
            // do sign up
            User user = User.builder()
                    .email(email)
                    .password(password)
                    .build();

            // attempt to create user
            if(userService.signUpEmailAndPassword(user) != null) {
                return ResponseEntity.status(HttpStatus.OK).body("Registration complete. You can sign in now.");
            }

            // non traced error
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("There was a problem with your registration. Try again later.");
        }

        // user already exists
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("This E-mail address is already used.");
    }
}
