package com.mntn.services;

import com.mntn.pojo.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface UserService extends UserDetailsService {
    User getUserByUsername(String username);
    User getUserById(String id);
    User register(Map<String, String> params, MultipartFile avatar);
    User updateUser(String id, Map<String, String> updates, MultipartFile avatar);
    boolean authenticate(String username, String password);
    List<User> getUsers();
}
