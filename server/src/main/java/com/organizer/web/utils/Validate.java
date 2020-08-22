package com.organizer.web.utils;

import com.organizer.core.model.User;
import org.springframework.stereotype.Component;

@Component
public class Validate {
    public boolean checkAuthUser(User user ){
        if(user == null){
            return false;
        }
        if(user.getVerifiedPhone()==true){
            return true;
        }
        return false;
    }
}
