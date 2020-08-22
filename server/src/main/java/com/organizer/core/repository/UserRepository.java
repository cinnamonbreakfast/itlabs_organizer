package com.organizer.core.repository;

import com.organizer.core.model.User;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends Repository<Long, User> {
    User findByEmailAndPassword(String username, String password);
    User findByPhone(String phone);
    User findByEmail(String email);
    @Query("select u from User u where u.email=?1 or u.phone=?2")
    User findByEmailOrPhone(String email, String phone);
}
