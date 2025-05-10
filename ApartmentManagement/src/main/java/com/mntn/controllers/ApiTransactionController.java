package com.mntn.controllers;

import com.mntn.pojo.Transaction;
import com.mntn.pojo.User;
import com.mntn.services.TransactionService;
import com.mntn.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/api/secure")
public class ApiTransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getTransactionsByUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(402).body(null);
        }
        String username = auth.getName();
        User user = userService.getUserByUsername(username);
        String userId = user.getId();

        List<Transaction> transactions = transactionService.getTransactionsByUserId(userId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Transaction>> getTransactionsByUserIdStatusAndCategory(
            @RequestParam(name = "status") String status,
            @RequestParam(name = "categoryId") String categoryId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(402).body(null);
        }
        String username = auth.getName();
        User user = userService.getUserByUsername(username);
        String userId = user.getId();

        List<Transaction> transactions = transactionService.getTransactionsByUserIdStatusAndCategory(userId, status, categoryId);
        return ResponseEntity.ok(transactions);
    }
}
