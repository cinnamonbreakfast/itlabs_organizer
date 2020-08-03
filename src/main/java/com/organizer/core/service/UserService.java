package com.organizer.core.service;

import com.organizer.core.model.User;
import com.organizer.core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }
}
