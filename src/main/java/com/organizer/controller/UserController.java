package com.organizer.controller;

import com.organizer.core.model.User;
import com.organizer.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/u/p/{phone}", method = RequestMethod.GET)
    public User findByPhone(@PathVariable String phone)
    {
        return userService.findByPhone(phone);
    }
}
