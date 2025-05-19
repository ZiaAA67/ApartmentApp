package com.mntn.controllers;

import com.mntn.pojo.Transaction;
import com.mntn.pojo.User;
import com.mntn.services.TransactionService;
import com.mntn.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/api")
public class ApiTransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    @GetMapping("/secure/transactions")
    public ResponseEntity<?> getTransactions(@RequestParam Map<String, String> params) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bạn chưa đăng nhập!");
        }

        String username = auth.getName();
        User user = userService.getUserByUsername(username);
        params.put("userId", String.valueOf(user.getId()));

        try {
            List<Transaction> transactions = transactionService.getTransactions(params);
            return ResponseEntity.ok(transactions);
        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi hệ thống: " + ex.getMessage());
        }
    }
}
