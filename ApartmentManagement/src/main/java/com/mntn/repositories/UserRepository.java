package com.mntn.repositories;

import com.mntn.pojo.User;

public interface UserRepository {
    User getUserByUsername(String username);
    User register(User u);
}
