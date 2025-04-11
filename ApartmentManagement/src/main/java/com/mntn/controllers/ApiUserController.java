package com.mntn.controllers;

import com.mntn.pojo.User;
import com.mntn.services.UserService;
import jakarta.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiUserController {
    @Autowired
    private UserService userDetailsService;

    @PostMapping(path = "/users", consumes = MediaType.MULTIPART_FORM_DATA)
    public ResponseEntity<User> register(@RequestParam Map<String, String> params,
                                         @RequestParam(value = "avatar") MultipartFile avatar) {

        return new ResponseEntity<>(this.userDetailsService.register(params, avatar), HttpStatus.CREATED);
    }
}
