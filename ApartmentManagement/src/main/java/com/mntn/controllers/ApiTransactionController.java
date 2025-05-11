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
    public ResponseEntity<List<Transaction>> getTransactions(@RequestParam Map<String, String> params) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).body(null);
        }

        String username = auth.getName();
        User user = userService.getUserByUsername(username);
        params.put("userId", String.valueOf(user.getId()));

        String fromDate = params.get("fromDate");
        String toDate = params.get("toDate");
        String categoryId = params.get("categoryId");
        String methodId = params.get("methodId");

        if (fromDate != null && !fromDate.isEmpty()) {
            params.put("fromDate", fromDate);
        }
        if (toDate != null && !toDate.isEmpty()) {
            params.put("toDate", toDate);
        }
        if (categoryId != null && !categoryId.isEmpty()) {
            params.put("categoryId", categoryId);
        }
        if (methodId != null && !methodId.isEmpty()) {
            params.put("methodId", methodId);
        }

        List<Transaction> transactions = transactionService.getTransactions(params);
        return ResponseEntity.ok(transactions);
    }
}
