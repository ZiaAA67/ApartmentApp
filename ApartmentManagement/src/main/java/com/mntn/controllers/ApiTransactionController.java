package com.mntn.controllers;

import com.mntn.pojo.Transaction;
import com.mntn.pojo.User;
import com.mntn.services.TransactionService;
import com.mntn.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiTransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    @GetMapping("/secure/transactions")
    public ResponseEntity<List<Transaction>> getTransactionsByCurrentUser(
            Principal principal,
            @RequestParam(value = "status", required = false, defaultValue = "completed") String status,
            @RequestParam(value = "categoryId", required = false) String categoryId) {

        // Lấy username từ token đăng nhập
        String username = principal.getName();

        User currentUser = userService.getUserByUsername(username);

        if (currentUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String userId = currentUser.getId();

        List<Transaction> transactions = transactionService.getTransactionsByUserIdStatusAndCategory(userId, status, categoryId);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
}
