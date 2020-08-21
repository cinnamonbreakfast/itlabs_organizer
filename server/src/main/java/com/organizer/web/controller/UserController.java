package com.organizer.web.controller;

import com.organizer.core.model.User;
import com.organizer.core.model.ValidationCode;
import com.organizer.core.service.UserService;
import com.organizer.core.service.ValidationCodeService;
import com.organizer.core.utils.Hash;
import com.organizer.web.auth.AuthStore;
import com.organizer.web.auth.JWToken;
import com.organizer.web.dto.SignUpDTO;
import com.organizer.web.dto.UserDTO;
import com.organizer.web.utils.Emailer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class UserController {
    private final UserService userService;
    private final AuthStore authStore;
    private final Emailer emailer;
    private final ValidationCodeService validationCodeService;

    @Autowired
    public UserController(UserService userService, AuthStore authStore, Emailer emailer, ValidationCodeService validationCodeService) {
        this.userService = userService;
        this.authStore = authStore;
        this.emailer = emailer;
        this.validationCodeService = validationCodeService;
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ResponseEntity<String> testfnc() {
        return new ResponseEntity<>("Hello!", HttpStatus.OK);
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
    public ResponseEntity<UserDTO> authenticate(@RequestParam(required = true) String email, @RequestParam(required = true, name = "password") String password) {
        User authUser = userService.findByEmail(email);

        if(authUser != null) {
            // bad credentials
            String hashPass= Hash.md5(password);
            if(!authUser.getPassword().equals(hashPass))
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).header("MESSAGE", "Wrong username or password.").body(null);

            // create a token and return it

            String token = JWToken.create(authUser.getEmail());
            Date authTime = new Date(JWToken.ttlMillis+System.currentTimeMillis());
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("TOKEN", token);
            responseHeaders.set("AUTH_TIME", authTime.toString());

            UserDTO authResponseUser = UserDTO.builder()
                    .email(authUser.getEmail())
                    .name(authUser.getName())
                    .phone(authUser.getPhone())
                    .role(authUser.getRole())
                    .imageURL(authUser.getImageURL())
                    .city(authUser.getCity())
                    .country(authUser.getCountry())
                    .token(token)
                    .authTime(authTime.toString())
                    .build();

            return ResponseEntity.ok()
                    .headers(responseHeaders)
                    .body(authResponseUser);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).header("MESSAGE", "Wrong username or password.").body(null);
    }

    @RequestMapping(value = "u/validate", method = RequestMethod.POST, consumes = "multipart/form")
    public ResponseEntity<String> validate(@RequestParam(name = "code") Integer code, @RequestParam(name = "purpose") String purpose) {
        ValidationCode dbCode = this.validationCodeService.find(code);
        return null;
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

            user = userService.signUpEmailAndPassword(user);

            if(user != null) {
                ValidationCode code = this.validationCodeService.createNewCode(user, "signup");

                if(code != null) {
                    this.emailer.sendSimpleMessage(user.getEmail(), "Hey, " + user.getName() + "! Your Validation Code arrived.", "Code: " + code.getCode());

                    return ResponseEntity.status(HttpStatus.OK).body("Registration complete. You can sign in now.");
                }
                // New code generation failed for some CHECK CONSOLE reason

                userService.remove(user); // kinda-like a roll back

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Something went wrong. Please, contact platform admin.");
            }

            // non traced error
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("There was a problem with your registration. Try again later.");
        }

        // user already exists
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("This E-mail address is already used.");
    }

    @RequestMapping(value = "u/changeName",method = RequestMethod.PUT)
    public ResponseEntity<String>changeName(@RequestParam String email,@RequestParam String name){
        User user =  userService.findByEmail(email);
        user.setName(name);
        if(userService.saveOrUpdate(user)!=null)
            return ResponseEntity.ok().body("Name changed.");
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something happened");
    }
}
