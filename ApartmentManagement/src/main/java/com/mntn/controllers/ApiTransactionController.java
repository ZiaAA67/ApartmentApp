package com.mntn.controllers;

import com.mntn.pojo.Transaction;
import com.mntn.pojo.User;
import com.mntn.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiTransactionController {

    @Autowired
    private TransactionService transactionService;

    // Lấy danh sách giao dịch của người dùng theo trạng thái
    @GetMapping("/secure/transactions")
    public ResponseEntity<List<Transaction>> getTransactionsByUserAndStatus(
            @RequestParam(value = "status", required = false) String status, Principal principal) {

        User user = new User();
        user.setUsername(principal.getName());

        List<Transaction> transactions = transactionService.getTransactionsByUserAndStatus(user, status);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
}
