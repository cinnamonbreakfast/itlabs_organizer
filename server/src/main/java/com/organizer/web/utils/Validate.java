package com.organizer.web.utils;

import com.organizer.core.model.User;

public class Validate {
    public static boolean checkAuthUser(User user ){
        if(user.getVerifiedEmail()==1)
            return true;
        return false;
    }
}
