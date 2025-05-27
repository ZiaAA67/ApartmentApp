package com.mntn.controllers;

import com.mntn.pojo.User;
import com.mntn.services.DeliveryService;
import com.mntn.services.UserService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiDeliveryController {

    @Autowired
    private DeliveryService deliveryService;

    @PostMapping("/secure/admin/delivery/create")
    public ResponseEntity<Object> createDelivery(@RequestBody Map<String, String> params) {
        return ResponseEntity.ok(deliveryService.createDelivery(params));
    }

    @PatchMapping("/secure/admin/delivery/update-status")
    public ResponseEntity<Object> updateStatusDelivery(@RequestParam("deliveryId") String deliveryId,
            @RequestParam("status") String status) {
        return ResponseEntity.ok(deliveryService.updateStatusDelivery(deliveryId, status));
    }

    @GetMapping("/secure/admin/deliveries")
    public ResponseEntity<Object> getDeliveryAdmin(@RequestParam Map<String, String> params) {
        return ResponseEntity.ok(deliveryService.getDeliveryAdmin(params));
    }

    @Autowired
    private UserService userService;

    @GetMapping("/secure/deliveries")
    public ResponseEntity<Object> getDelivery(@RequestParam Map<String, String> params) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.getUserByUsername(username);
        params.put("userId", String.valueOf(user.getId()));
        return ResponseEntity.ok(deliveryService.getDelivery(params));
    }
}
