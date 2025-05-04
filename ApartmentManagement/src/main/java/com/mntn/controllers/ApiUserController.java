package com.mntn.controllers;

import com.mntn.pojo.User;
import com.mntn.services.UserService;
import com.mntn.utils.JwtUtils;
import jakarta.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiUserController {
    @Autowired
    private UserService userDetailsService;

    // Đăng ký user mới
    @PostMapping(path = "/users", consumes = MediaType.MULTIPART_FORM_DATA)
    public ResponseEntity<User> register(@RequestParam Map<String, String> params,
                                         @RequestParam(value = "avatar", required = false) MultipartFile avatar) {
        return new ResponseEntity<>(this.userDetailsService.register(params, avatar), HttpStatus.CREATED);
    }

    // Chứng thực JWT, trả về token nếu thông tin đăng nhập chính xác
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User u) {
        if (this.userDetailsService.authenticate(u.getUsername(), u.getPassword())) {
            try {
                String token = JwtUtils.generateToken(u.getUsername());
                return ResponseEntity.ok().body(Collections.singletonMap("token", token));
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Lỗi khi tạo JWT");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Thông tin đăng nhập không chính xác!");
    }

    // Lấy user bằng id
    @GetMapping("/secure/users/{id}")
    public ResponseEntity<User> retrieve(@PathVariable(value = "id") String id) {
        return new ResponseEntity<>(this.userDetailsService.getUserById(id), HttpStatus.OK);
    }

    // Lấy thông tin profile, yêu cầu phải đăng nhập
    @RequestMapping("/secure/profile")
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<User> getProfile(Principal principal) {
        return new ResponseEntity<>(this.userDetailsService.getUserByUsername(principal.getName()), HttpStatus.OK);
    }

    // Update user -> gửi bằng form-data
    @PatchMapping("/secure/users/{id}")
    public ResponseEntity<User> update(@PathVariable(value = "id") String id,
                                       @RequestParam Map<String, String> updates,
                                       @RequestParam(value = "avatar", required = false) MultipartFile avatar) {
        return new ResponseEntity<>(this.userDetailsService.updateUser(id, updates, avatar), HttpStatus.OK);
    }

    // Lấy thông tin tất cả users
    @GetMapping("/secure/users")
    public ResponseEntity<List<User>> list() {
        return new ResponseEntity<>(this.userDetailsService.getUsers(), HttpStatus.OK);
    }
}
