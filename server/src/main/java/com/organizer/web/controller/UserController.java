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
import com.organizer.web.utils.Smser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class UserController {
    private final UserService userService;
    private final AuthStore authStore;
    private final Emailer emailer;
    private final ValidationCodeService validationCodeService;
    private final Smser smser;
    @Autowired
    public UserController(UserService userService, AuthStore authStore, Emailer emailer, ValidationCodeService validationCodeService, Smser smser) {
        this.userService = userService;
        this.authStore = authStore;
        this.emailer = emailer;
        this.validationCodeService = validationCodeService;
        this.smser = smser;
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
                    .verifiedEmail(0)
                    .verifiedPhone(0)
                    .build();

            // attempt to create user

            try {
                user = userService.signUpEmailAndPassword(user);
            }
            catch (Exception e){
                return ResponseEntity.status(HttpStatus.OK).body("Uniq constrain violation");
            }

            if(user != null) {
                ValidationCode code = this.validationCodeService.createNewCode(user, "signup");

                if(code != null) {
                    //this.emailer.sendSimpleMessage(user.getEmail(), "Hey, " + user.getName() + "! Your Validation Code arrived.", "Code: " + code.getCode());
                    try {
                        this.smser.sendSms("+4"+user.getPhone(), "Hey " + user.getName() + " this is the verification code : " + code.getCode());
                        return ResponseEntity.status(HttpStatus.OK).body("Registration complete. You can sign in now.");
                    }
                    catch (Exception e){

                    }
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

//
    @RequestMapping(value = "u/reset/{method}",method = RequestMethod.POST)
    public ResponseEntity<String>  resetPassword(@PathVariable String method,@RequestBody String contact)
    {
        if(method != null && !method.isEmpty()) {
            if(method.equals("phone")) {
                User target = this.userService.findByPhone(contact);

                if(target != null) {

                    if(target.getVerifiedPhone()!=1)
                    {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not a verified phone number");
                    }

                    ValidationCode code = this.validationCodeService.createNewCode(target, "reset_pass");

                    if(code != null) {
                        this.smser.sendSms(contact, code.getCode() + " is the code for password reset on AppointmentApp, " + target.getName() + ".");

                        return ResponseEntity.status(HttpStatus.OK).body("Validation code sent to " + contact + ".");
                    }

                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong. Try again later or contact the admins.");
                }

                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No user found by this phone number.");
            }
            else if(method.equals("email")) {
                User target = this.userService.findByEmail(contact);


                if(target != null) {

                    if(target.getVerifiedEmail()!=1){
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not a verified mail specified");
                    }
                    ValidationCode code = this.validationCodeService.createNewCode(target, "reset_pass");

                    if(code != null) {
                        this.emailer.sendSimpleMessage(contact, "Password reset link",  code.getCode() + " is the code for password reset on AppointmentApp, " + target.getName() + ".");

                        return ResponseEntity.status(HttpStatus.OK).body("Validation code sent to " + contact + ".");
                    }

                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong. Try again later or contact the admins.");
                }

                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No user found by this email.");
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Recover method not specified.");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Try again later.");
    }
    @RequestMapping(value ="u/resetChect")
    public ResponseEntity<String> checkResetCode(@RequestParam String method,String contact,Integer code){

        if(method != null && !method.isEmpty()) {
            if(method.equals("phone")){
                User target = userService.findByPhone(contact);
                if(target ==null){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not a known phone number");
                }
                if(target.getVerifiedPhone()!=1)
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not a verified phone number");
               ValidationCode validationCode = validationCodeService.findByCodeAndPhone(code,contact);

               if(validationCode==null){
                   return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not a valid code");
               }
               LocalDate currDate = LocalDate.now();



            }else if(method.equals("mail")){
                User target = userService.findByPhone(contact);

            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Recover method not specified");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Try again later.");
    }



}
