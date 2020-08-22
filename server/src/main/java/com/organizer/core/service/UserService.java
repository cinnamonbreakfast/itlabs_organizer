package com.organizer.core.service;

import com.organizer.core.model.User;
import com.organizer.core.repository.UserRepository;
import com.organizer.core.utils.Hash;
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

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User emailAuth(String email, String password) {
//        return userRepository.findByEmailAndPassword(email, Hash.md5(password));
        return userRepository.findByEmailAndPassword(email, (password));
    }

    public User signUpEmailAndPassword(User user) {
        user.setPassword(Hash.md5(user.getPassword()));
        return userRepository.save(user);
    }

    public void remove(User user) {
        this.userRepository.delete(user);
    }

    public User findById(Long id){
        return userRepository.findById(id).get();
    }
    public User saveOrUpdate(User user ){
        return userRepository.save(user);
    }
    public User findByEmailOrPhone(String email, String phone){
        return userRepository.findByEmailOrPhone(email, phone);
    }
}