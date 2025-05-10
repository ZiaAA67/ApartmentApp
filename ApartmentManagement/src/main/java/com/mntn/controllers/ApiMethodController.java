package com.mntn.controllers;

import com.mntn.pojo.Method;
import com.mntn.services.MethodService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiMethodController {

    @Autowired
    private MethodService methodService;

    @GetMapping("/method")
    public List<Method> getMethod(@RequestParam(name = "isActive", required = false) Boolean isActive) {
        if (isActive == null) {
            isActive = true;
        }
        return methodService.getMethod(isActive);
    }
}
