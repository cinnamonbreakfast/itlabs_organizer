package com.organizer.web.controller;

import com.organizer.core.model.Prefix;
import com.organizer.core.model.User;
import com.organizer.core.model.ValidationCode;
import com.organizer.core.service.PrefixService;
import com.organizer.core.service.UserService;
import com.organizer.core.service.ValidationCodeService;
import com.organizer.core.utils.Hash;
import com.organizer.web.auth.AuthStore;
import com.organizer.web.auth.JWToken;
import com.organizer.web.dto.ResponseDTO;
import com.organizer.web.dto.SignUpDTO;
import com.organizer.web.dto.UserDTO;
import com.organizer.web.utils.Emailer;
import com.organizer.web.utils.Regex;
import com.organizer.web.utils.Smser;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
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
    private final Regex regex;
    private final PrefixService prefixService;
    @Autowired
    public UserController(UserService userService, AuthStore authStore, Emailer emailer, ValidationCodeService validationCodeService, Smser smser, Regex regex, PrefixService prefixService) {
        this.userService = userService;
        this.authStore = authStore;
        this.emailer = emailer;
        this.validationCodeService = validationCodeService;
        this.smser = smser;
        this.regex=regex;
        this.prefixService = prefixService;
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
                    .country(find.getCountry())
                    .city(find.getCity())
                    .build();
            user.setId(find.getId());

            return ResponseEntity.ok(user);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    private ResponseEntity<ResponseDTO<UserDTO>> makeASession(User user) {
        String token = JWToken.create(user.getId());
        Date authTime = new Date(JWToken.ttlMillis+System.currentTimeMillis());
        UserDTO data = UserDTO.builder()
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .imageURL(user.getImageURL())
                .verifiedEmail(user.getVerifiedEmail())
                .verifiedPhone(user.getVerifiedPhone())
                .city(user.getCity())
                .country(user.getCountry())
                .token(token)
                .authTime(authTime.toString())
                .build();
        data.setId(user.getId());


        return ResponseEntity.ok()

                .body(
                        ResponseDTO.<UserDTO>builder()
                                .message("User authenticated successfully!")
                                .code(200)
                                .data(data)
                                .build()
                );
    }

    @RequestMapping(value = "u/signin", method = RequestMethod.POST)
    public ResponseEntity<ResponseDTO<UserDTO>> authenticate(@RequestParam String contact, @RequestParam String password) {
        int m = regex.phoneMatcher("40",contact);
        String tel = "+40"+contact.substring(m,contact.length());
        User authUser = userService.findByEmailOrPhone(contact, tel);
        System.out.println(authUser);
        if(authUser != null) {
            String hashPass = Hash.md5(password);

            if(!authUser.getPassword().equals(hashPass))
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).header("MESSAGE", "Wrong username or password.").body(null);

            // create a token and return it

            return makeASession(authUser);
        }

        return ResponseEntity.ok()
                .body(
                        ResponseDTO.<UserDTO>builder().message("Invalid email, phone or password.").code(400).build()
                );
    }

    // TODO: add prefix
    @RequestMapping(value = "u/validate", method = RequestMethod.POST, consumes = "multipart/form-data")
    public ResponseEntity<ResponseDTO<Integer>> validate(@RequestParam Integer code, @RequestParam String purpose, @RequestParam String contact) {
        ValidationCode dbCode = this.validationCodeService.find(code);


        int m = regex.phoneMatcher("40",contact);
        if(m==0){
            return ResponseEntity.status(HttpStatus.OK).body(ResponseDTO.<Integer>builder().message("Bring a valid phone number.").code(400).build());
        }
        String tel = "+40"+contact.substring(m,contact.length());
        if(dbCode != null && dbCode.getPurpose().equals(purpose) && dbCode.getAccount().getPhone().equals(tel)) {
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

    @RequestMapping(value = "u/presignup", method = RequestMethod.POST, consumes = "multipart/form-data")
    public ResponseEntity<ResponseDTO<Integer>> signUpCodeRequest(@RequestParam(required = true) String phone) {


            int m = regex.phoneMatcher("40",phone);
            if(m==0){
                return ResponseEntity.status(HttpStatus.OK).body(ResponseDTO.<Integer>builder().message("Bring a valid phone number.").code(400).build());
            }
            String tel = "+40"+phone.substring(m,phone.length());
            User target = this.userService.findByPhone(tel);

            if(target != null && target.getVerifiedPhone() != null) {
                return ResponseEntity.status(HttpStatus.OK).body(ResponseDTO.<Integer>builder().message("This phone number is already in use.").code(400).build());
            }
            target= User.builder()
                    .verifiedPhone(false)
                    .verifiedEmail(false).phone(tel).
                    build();
            target = this.userService.saveOrUpdate(target);
            ValidationCode code = this.validationCodeService.createNewCode(target, "sign_up", LocalDateTime.now().plusHours(1));

            String data = this.smser.sendSms(tel, code.getCode().toString() + " is the code for sign up process on AppointmentApp.");
            if(data ==null){
                try {
                    userService.remove(target);
                    validationCodeService.remove(code);
                }
                catch (Exception e){ }
                return ResponseEntity.status(HttpStatus.OK).body(ResponseDTO.<Integer>builder().message("Please, enter a valid phone number").code(400).build());
            }
            System.out.println(data);
            Object obj = JSONValue.parse(data);
            JSONObject jsonObject = (JSONObject) obj ;
            Object o = jsonObject.get("status");
            String str =o.toString();
            if(str.equals("200")){
                return ResponseEntity.status(HttpStatus.OK).body(ResponseDTO.<Integer>builder().message("A code has been sent to this phone number").code(200).build());
            }
            else{
                try {
                    userService.remove(target);
                    validationCodeService.remove(code);
                }
                catch (Exception e){ }
                return ResponseEntity.status(HttpStatus.OK).body(ResponseDTO.<Integer>builder().message("Please, enter a valid phone number").code(400).build());
            }

    }

    @RequestMapping(value = "u/recoveraction", method = RequestMethod.POST, consumes = "multipart/form-data")
    public ResponseEntity<ResponseDTO<Integer>> recoverAction(@RequestParam String code, @RequestParam String password) {
        ValidationCode existingCode = this.validationCodeService.find(Integer.parseInt(code));

        if(existingCode == null) {
            return ResponseEntity.ok(ResponseDTO.<Integer>builder().message("This validation code is invalid.").code(HttpStatus.FORBIDDEN.value()).build());
        }

        if(!existingCode.getPurpose().equals("pass_recover") || existingCode.getDueDate().isBefore(LocalDateTime.now())) {
            this.validationCodeService.cancel(existingCode);

            return ResponseEntity.ok(ResponseDTO.<Integer>builder().message("This validation code is invalid.").code(HttpStatus.FORBIDDEN.value()).build());
        }

        // todo: maybe check if it's the old password

        User target = existingCode.getAccount();
        target.setPassword(Hash.md5(password));
        this.userService.saveOrUpdate(target);

        this.validationCodeService.cancel(existingCode);

        return ResponseEntity.ok(ResponseDTO.<Integer>builder().message("Your new password has been set. You can sign in now.").code(200).build());
    }

    @RequestMapping(value = "u/recoverask", method = RequestMethod.POST, consumes = "multipart/form-data")
    public ResponseEntity<ResponseDTO<Integer>> recoverAsk(@RequestParam String method, @RequestParam String contact, @RequestParam String prefix) {
        if(method.equals("phone")) {
            // todo: validare
            User target = this.userService.findByPhone(prefix+contact);

            if(target == null) {
                return ResponseEntity.ok()
                        .body(
                                ResponseDTO.<Integer>builder().code(400).message("No user found by this phone number.").build()
                        );
            }

            ValidationCode phoneCode = this.validationCodeService.createNewCode(target, "pass_recover", LocalDateTime.now().plusHours(1));

            if(this.smser.sendSms(prefix+contact, phoneCode.getCode() + " is the validation code for password recovery process on AppointmentApp.") != null) {
                return ResponseEntity.ok()
                        .body(
                                ResponseDTO.<Integer>builder().message("A validation code has been sent to your phone number.").code(200).build()
                        );
            }

            return ResponseEntity.ok()
                    .body(
                            ResponseDTO.<Integer>builder().message("An unknown error has occured. Try again later.").build()
                    );
        } else if (method.equals("email")) {
            // todo: validare
            User target = this.userService.findByEmail(contact);

            if(target == null) {
                return ResponseEntity.ok().body(
                        ResponseDTO.<Integer>builder().message("No user found by this email.").code(400).build()
                );
            }

            ValidationCode emailCode = this.validationCodeService.createNewCode(target, "pass_recover", LocalDateTime.now().plusHours(2));

            this.emailer.sendSimpleMessage(contact, "Password recovery", emailCode.getCode() + " is the validation code for password recovery process on AppointmentApp.");

            return ResponseEntity.ok(ResponseDTO.<Integer>builder().message("An email containing recover methods has been sent to your email.").code(200).build());
        }

        return null;
    }

    @RequestMapping(value = "u/signup", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<ResponseDTO<UserDTO>> signUp(@RequestBody(required = true) SignUpDTO signUpDTO) {
        String contact =signUpDTO.getPhone();
        int m = regex.phoneMatcher("40",contact);
        if(m==0){
            return ResponseEntity.ok()
                    .body(
                            ResponseDTO.<UserDTO>builder().message("Invalid phone number.").code(400).build()
                    ); }
        String tel = "+40"+contact.substring(m,contact.length());
        System.out.println(tel);
        User existingUser = userService.findByPhone(tel);

        System.out.println(existingUser);
        if(existingUser == null) {
            return ResponseEntity.ok()
                    .body(
                            ResponseDTO.<UserDTO>builder().message("Invalid sign up process. Try again later.").code(400).build()
                    );
        }

        if (existingUser.getVerifiedPhone().equals(false)) {
            ValidationCode code = this.validationCodeService.find(signUpDTO.getCode());

            if(code == null) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(
                                ResponseDTO.<UserDTO>builder().code(400).message("This validation code that you entered does not exist.").build()
                        );
            }

            if(code.getDueDate().isBefore(LocalDateTime.now())) {
                this.validationCodeService.cancel(code);

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
            existingUser.setPassword(Hash.md5(signUpDTO.getPassword()));
            existingUser.setVerifiedPhone(true);
            existingUser.setCity(signUpDTO.getCity());
            existingUser.setCountry(signUpDTO.getCountry());

            if(this.userService.saveOrUpdate(existingUser) != null) {
                this.validationCodeService.cancel(code);

                if(existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
                    ValidationCode emailCode = this.validationCodeService.createNewCode(existingUser, "email", LocalDateTime.now().plusDays(15));

                    this.emailer.sendSimpleMessage(existingUser.getEmail(), "Code for Email Validation on AppointmentApp.", emailCode.getCode() + " is the code for your email validation on AppointmentApp. Remember, this code will expire in 15 days.");
                }

                return makeASession(existingUser);
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


    @RequestMapping(value = "u/changeDetails",method = RequestMethod.PUT)
    public ResponseEntity<String> changeDetails(@RequestParam SignUpDTO details,@RequestHeader String token){
        Long id = JWToken.checkToken(token);
        if(id==null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("invalid token");
        }
        User user = userService.findById(id);
        if(user == null)
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not a known user");
        }
        user.setName(details.getName());
        user.setCity(details.getCity());
        user.setCountry(details.getCountry());

        return ResponseEntity.ok("changed");

    }
}