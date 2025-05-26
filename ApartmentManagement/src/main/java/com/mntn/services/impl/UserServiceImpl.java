package com.mntn.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.mntn.pagination.PaginatedResponse;
import com.mntn.pojo.User;
import com.mntn.repositories.UserRepository;
import com.mntn.services.UserService;
import com.mntn.utils.EmailUtil;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service("userDetailsService")
public class UserServiceImpl implements UserService {
    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public User getUserByUsername(String username) {
        return this.userRepo.getUserByUsername(username);
    }

    @Override
    public User getUserById(String id) {
        return this.userRepo.getUserById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = this.getUserByUsername(username);
        if (u == null) {
            throw new UsernameNotFoundException("Invalid username!");
        }

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(u.getRole()));

        return new org.springframework.security.core.userdetails.User(
                u.getUsername(), u.getPassword(), authorities);
    }

    @Override
    public User register(Map<String, String> params, MultipartFile avatar) {
        String username = params.get("username");
        String phone = params.get("phone");
        String email = params.get("email");

        if (userRepo.existsByUsername(username)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username '" + username + "' đã tồn tại!");
        }
        if (userRepo.existsByPhone(phone)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number '" + phone + "' đã tồn tại!");
        }
        if (email != null && !email.isEmpty() && userRepo.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email '" + email + "' đã tồn tại!");
        }

        User u = new User();
        u.setId(UUID.randomUUID().toString());
        u.setFirstName(params.get("firstName"));
        u.setLastName(params.get("lastName"));
        u.setUsername(username);
        u.setIdentityNumber(params.get("identityNumber"));
        u.setPhone(phone);
//        u.setPassword(this.passwordEncoder.encode(params.get("password")));

        String password = UUID.randomUUID().toString();
        try {
            EmailUtil.sendEmail(email, "Day la mat khau cua ban", password);
        } catch (MessagingException e) {
            throw new IllegalArgumentException(e);
        }
        u.setPassword(this.passwordEncoder.encode(password));

        u.setIsActive(true);
        u.setIsFirstLogin(true);

        if (email != null && !email.isEmpty()) {
            u.setEmail(email);
        }

        String address = params.get("address");
        if(address != null && !address.isEmpty()) {
            u.setAddress(address);
        }

        String gender = params.get("gender");
        if(gender != null && !gender.isEmpty()) {
            u.setGender(Short.valueOf(gender));
        }

        String birth = params.get("birth");
        if (birth != null && !birth.isEmpty()) {
            try {
                Date birthDate = new SimpleDateFormat("dd-MM-yyyy").parse(birth);
                u.setBirth(birthDate);
            } catch (ParseException e) {
                Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, "Birth format must be (dd-MM-yyyy)");
            }
        }

        String role = params.get("role");
        if(role != null && !role.isEmpty())
            u.setRole(role);
        else
            u.setRole("ROLE_RESIDENT");

        if (avatar != null && !avatar.isEmpty()) {
            try {
                Map res = cloudinary.uploader().upload(avatar.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                u.setAvatar(res.get("secure_url").toString());
            } catch (IOException ex) {
                Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            u.setAvatar("https://res.cloudinary.com/dbmwgavqz/image/upload/v1748252972/avt_vvnc8k.jpg");
        }

        u.setCreatedDate(new Date());

        return this.userRepo.register(u);
    }

    @Override
    public User updateUser(String id, Map<String, String> updates, MultipartFile avatar) {
        User u = userRepo.getUserById(id);

        if (updates != null) {
            updates.forEach((field, value) -> {
                switch (field) {
                    case "isActive" -> u.setIsActive(Boolean.parseBoolean(value));
                    case "firstName" -> u.setFirstName(value);
                    case "lastName" -> u.setLastName(value);
                    case "email" -> u.setEmail(value);
                    case "phone" -> u.setPhone(value);
                    case "password" -> u.setPassword(passwordEncoder.encode(value));
                }
            });
        }

        if (avatar != null && !avatar.isEmpty()) {
            try {
                Map res = cloudinary.uploader().upload(avatar.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                u.setAvatar(res.get("secure_url").toString());
            } catch (IOException ex) {
                Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return this.userRepo.updateUser(u);
    }

    @Override
    public boolean authenticate(String username, String password) {
        return this.userRepo.authenticate(username, password);
    }

    @Override
    public PaginatedResponse<User> getUsers(Map<String, String> params) {
        return this.userRepo.getUsers(params);
    }

}