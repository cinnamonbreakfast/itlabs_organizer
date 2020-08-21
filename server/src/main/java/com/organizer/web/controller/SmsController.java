package com.organizer.web.controller;

import com.organizer.core.service.sms.SmsSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SmsController {
    private final SmsSender smsSender;

    @Autowired
    public SmsController(SmsSender smsSender){
        this.smsSender= smsSender;
    }
    @RequestMapping(value = "sms/send",method = RequestMethod.POST)
    public ResponseEntity<String>  sendSms(@RequestParam String sender, @RequestParam String body){
        smsSender.sendSms(sender,body);
        return ResponseEntity.ok("done");
    }


}
