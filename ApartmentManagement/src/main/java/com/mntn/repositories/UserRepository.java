package com.mntn.repositories;

import com.mntn.pojo.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface UserRepository {
    boolean existsByUsername(String username);
    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);

    User getUserByUsername(String username);
    User getUserById(String id);
    User register(User u);
    User updateUser(User u);
    boolean authenticate(String username, String password);
    List<User> getUsers();
}
