package com.mntn.repositories;

import com.mntn.pojo.User;

import java.util.List;

public interface UserRepository {
    User getUserByUsername(String username);
    User register(User u);
    boolean authenticate(String username, String password);
    List<User> getUsers();
}
