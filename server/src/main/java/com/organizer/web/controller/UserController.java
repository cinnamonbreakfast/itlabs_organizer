package com.organizer.web.controller;

import com.organizer.core.model.User;
import com.organizer.core.model.ValidationCode;
import com.organizer.core.service.UserService;
import com.organizer.core.service.ValidationCodeService;
import com.organizer.core.utils.Hash;
import com.organizer.web.auth.AuthStore;
import com.organizer.web.auth.JWToken;
import com.organizer.web.dto.ResponseDTO;
import com.organizer.web.dto.SignUpDTO;
import com.organizer.web.dto.UserDTO;
import com.organizer.web.utils.Emailer;
import com.organizer.web.utils.Smser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

    private ResponseEntity<ResponseDTO<UserDTO>> makeASession(User user) {
        UserDTO data = UserDTO.builder()
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .imageURL(user.getImageURL())
                .role(user.getRole())
                .verifiedEmail(user.getVerifiedEmail())
                .verifiedPhone(user.getVerifiedPhone())
                .city(user.getCity())
                .country(user.getCountry())
                .build();

        String token = JWToken.create(user.getId().);
        Date authTime = new Date(JWToken.ttlMillis+System.currentTimeMillis());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("TOKEN", token);
        responseHeaders.set("AUTH_TIME", authTime.toString());

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(
                        ResponseDTO.<UserDTO>builder()
                                .message("User authenticated successfully!")
                                .code(200)
                                .data(data)
                                .build()
                );
    }

    @RequestMapping(value = "u/auth", method = RequestMethod.POST)
    public ResponseEntity<UserDTO> authenticate(@RequestParam(required = true) String contact, @RequestParam(required = true, name = "password") String password) {
        User authUser = userService.findByEmailOrPhone(contact,contact);

        if(authUser != null) {
            // bad credentials
            String hashPass= Hash.md5(password);
            if(!authUser.getPassword().equals(hashPass))
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).header("MESSAGE", "Wrong username or password.").body(null);

            // create a token and return it

            String token = JWToken.create(authUser.getId());
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

    @RequestMapping(value = "u/validate", method = RequestMethod.POST, consumes = "multipart/form-data")
    public ResponseEntity<ResponseDTO<Integer>> validate(@RequestParam Integer code, @RequestParam String purpose, @RequestParam String contact) {
        ValidationCode dbCode = this.validationCodeService.find(code);

        if(dbCode != null && dbCode.getPurpose().equals(purpose) && dbCode.getAccount().getPhone().equals(contact)) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            ResponseDTO.<Integer>builder()
                                    .message("This code is still valid.")
                                    .code(200)
                                    .build()
                    );
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ResponseDTO.<Integer>builder()
                        .message("Invalid code.")
                        .build()
                );
    }

    @RequestMapping(value = "u/presigup", method = RequestMethod.POST, consumes = "multipart/form-data")
    public ResponseEntity<ResponseDTO<Integer>> signUpCodeRequest(@RequestParam String phone) {
        if(phone != null && phone.length() == 12) {
            User target = this.userService.findByPhone(phone);

            if(target != null && target.getVerifiedPhone() != null) {
                return ResponseEntity.status(HttpStatus.OK).body(ResponseDTO.<Integer>builder().message("This phone number is already in use.").code(400).build());
            }

            target = User.builder().phone(phone).build();
            target = this.userService.saveOrUpdate(target);
            ValidationCode code = this.validationCodeService.createNewCode(target, "sign_up");

            this.smser.sendSms(phone, code.getCode().toString() + " is the code for sign up process on AppointmentApp.");

            return ResponseEntity.status(HttpStatus.OK).body(ResponseDTO.<Integer>builder().message("A code has been sent to this phone number").code(200).build());
        }

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDTO.<Integer>builder().message("Please, enter a valid phone number").code(400).build());
    }

    @RequestMapping(value = "u/signup", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<ResponseDTO<UserDTO>> signUp(@RequestBody(required = true) SignUpDTO signUpDTO) {
        User existingUser = userService.findByPhone(signUpDTO.getPhone());

        if (!existingUser.getVerifiedPhone().equals(1)) {
            ValidationCode code = this.validationCodeService.find(signUpDTO.getCode());

            if(code == null) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(
                                ResponseDTO.<UserDTO>builder().code(400).message("This validation code that you entered does not exist.").build()
                        );
            }

<<<<<<< HEAD
            if(code.getDueDate().isBefore(LocalDateTime.now())) {
                this.validationCodeService.cancel(code);
=======
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
>>>>>>> server_cipy

                return ResponseEntity.status(HttpStatus.OK)
                        .body(
                                ResponseDTO.<UserDTO>builder().code(400).message("Validation code is expired. Please, request for a new one.").build()
                        );
            }

            if(!code.getPurpose().equals("sign_up")) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(
                                ResponseDTO.<UserDTO>builder().code(400).message("This code is not valid. Try getting a new one again.").build()
                        );
            }

            existingUser.setName(signUpDTO.getName());
            existingUser.setEmail(signUpDTO.getEmail());
            existingUser.setPassword(signUpDTO.getPassword());
            existingUser.setVerifiedPhone(1);
            existingUser.setCity(signUpDTO.getCity());
            existingUser.setCountry(signUpDTO.getCountry());

            if(this.userService.saveOrUpdate(existingUser) != null) {
                this.validationCodeService.cancel(code);

                return ResponseEntity.status(HttpStatus.OK)
                        .body(
                                ResponseDTO.<UserDTO>builder().code(200).message("Account registered successfully. You will be signed in.").build()
                        );
            }
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ResponseDTO.<UserDTO>builder().message("This user already exists.").build()
                );
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
<<<<<<< HEAD
=======

//
    @RequestMapping(value = "u/reset/{method}",method = RequestMethod.POST)
    public ResponseEntity<String>  resetPassword(@PathVariable String method,@RequestBody String contact)
    {
        if(method != null && !method.isEmpty()) {
            if(method.equals("phone")) {
                User target = this.userService.findByPhone(contact);

                if(target != null) {

                    if(target.getVerifiedPhone()==false)
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

                    if(target.getVerifiedEmail()==false){
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
                if(target.getVerifiedPhone()==false)
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



>>>>>>> server_cipy
}
