package com.mntn.controllers;

import com.mntn.pojo.Transaction;
import com.mntn.pojo.User;
import com.mntn.pojo.momo.PaymentResponse;
import com.mntn.services.TransactionService;
import com.mntn.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @GetMapping("/secure/transactions/{apartmentId}")
    public ResponseEntity<?> getTransactionsByApartmentId(@RequestParam("userId") String userId, @PathVariable("apartmentId") String apartmentId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bạn chưa đăng nhập!");
        }

        String username = auth.getName();
        User user = userService.getUserByUsername(username);
        userId = user.getId();

        List<Transaction> transactions = transactionService.getTransactionsByApartmentId(userId, apartmentId);
        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/secure/transactions/pay/{transactionId}")
    public ResponseEntity<?> payTransaction(@PathVariable("transactionId") String transactionId) {
        try {
            if (transactionId == null || transactionId.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("transactionId không hợp lệ!");
            }

            PaymentResponse response = transactionService.payTransaction(transactionId);

            return ResponseEntity.ok(Map.of("payUrl", response.getPayUrl()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lỗi: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi hệ thống: " + ex.getMessage());
        }
    }
}
