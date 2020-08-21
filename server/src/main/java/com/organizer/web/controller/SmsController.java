package com.organizer.web.controller;

import com.organizer.web.utils.AuthSession;
import com.organizer.web.utils.Smser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SmsController {
    private final Smser smsSender;

    @Autowired
    public SmsController(Smser smsSender){
        this.smsSender= smsSender;
    }
    @RequestMapping(value = "sms/send",method = RequestMethod.POST)
    public ResponseEntity<String>  sendSms(@RequestParam String sender, @RequestParam String body){
        smsSender.sendSms("","");
        return ResponseEntity.ok("done");
    }


}
