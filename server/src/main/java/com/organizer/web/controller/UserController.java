package com.organizer.web.controller;

import com.organizer.core.model.User;
import com.organizer.core.service.UserService;
import com.organizer.web.auth.AuthStore;
import com.organizer.web.dto.SignUpDTO;
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

    @RequestMapping(value = "/u/search/{email}", method = RequestMethod.GET)
    public ResponseEntity<UserDTO> findByEmail(@PathVariable(required = true) String email) {
        User find = userService.findByEmail(email);

        if(find != null) {
            UserDTO user = UserDTO.builder()
                    .name(find.getName())
                    .email(find.getEmail())
                    .imageURL(find.getImageURL())
                    .phone(find.getPhone())
                    .role(find.getRole())
                    .country(find.getCountry())
                    .city(find.getCity())
                    .build();
            user.setId(find.getId());

            return ResponseEntity.ok(user);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @RequestMapping(value = "u/auth", method = RequestMethod.POST)
    public ResponseEntity<UserDTO> authenticate(@RequestParam(required = true) String email, @RequestParam(required = true) String password) {
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

    @RequestMapping(value = "u/signup", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<String> signUp(@RequestBody(required = true) SignUpDTO signUpDTO) {
        User existingUser = userService.findByEmail(signUpDTO.getEmail());

        System.out.println(signUpDTO);

        if(existingUser == null) {
            // do sign up
            User user = User.builder()
                    .email(signUpDTO.getEmail())
                    .name(signUpDTO.getName())
                    .phone(signUpDTO.getPhone())
                    .role(signUpDTO.getRole())
                    .city(signUpDTO.getCity())
                    .country(signUpDTO.getCountry())
                    .password(signUpDTO.getPassword())
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
