package com.organizer.core.repository;

import com.organizer.core.model.User;

public interface UserRepository extends Repository<Long, User> {
    User   findByEmailAndPassword(String username, String password);
    User findByPhone(String phone);
    User findByEmail(String email);
}
