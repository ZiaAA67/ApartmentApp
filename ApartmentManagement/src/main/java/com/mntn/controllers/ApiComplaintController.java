package com.mntn.controllers;

import com.mntn.pojo.Complaint;
import com.mntn.pojo.User;
import com.mntn.services.ComplaintService;
import com.mntn.services.UserService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiComplaintController {

    @Autowired
    ComplaintService complaintService;

    @Autowired
    private UserService userService;

    @PostMapping("/secure/complaint/create")
    public ResponseEntity<Object> createComplaint(@RequestBody Map<String, String> params) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bạn chưa đăng nhập!");
        }

        String username = auth.getName();
        User user = userService.getUserByUsername(username);
        params.put("userId", String.valueOf(user.getId()));
        try {
            return new ResponseEntity<>(this.complaintService.createComplaint(params), HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping("/secure/admin/complaints")
    public ResponseEntity<?> getComplaints(@RequestParam Map<String, String> params) {
        try {
            List<Complaint> complaints = complaintService.getComplaints(params);
            return ResponseEntity.ok(complaints);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi server: " + ex.getMessage());
        }
    }

    @PatchMapping("/secure/admin/complaint/update-status/{complaintId}")
    public ResponseEntity<?> updateStatusComplaint(@PathVariable("complaintId") String complaintId,
            @RequestParam("status") String status) {
        try {
            return ResponseEntity.ok(complaintService.updateComplaint(complaintId, status));
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(500).body("Server Error: " + ex.getMessage());
        }
    }
}
