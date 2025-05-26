package com.mntn.controllers;

import com.mntn.dto.TransactionDTO;
import com.mntn.pojo.Transaction;
import com.mntn.pojo.User;
import com.mntn.pojo.momo.PaymentResponse;
import com.mntn.services.TransactionService;
import com.mntn.services.UserService;
import jakarta.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

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
            ex.printStackTrace();
            return ResponseEntity.status(500).body("Server Error: " + ex.getMessage());
        }
    }

    // Lấy unpaid
    @GetMapping("/secure/admin/transactions")
    public ResponseEntity<?> getTransactionsByAdmin(@RequestParam Map<String, String> params) {
        try {
            List<Transaction> transactions = transactionService.getTransactionsByAdmin(params);
            return ResponseEntity.ok(transactions);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(500).body("Server Error: " + ex.getMessage());
        }
    }

    // Thanh toán
    // https://developers.momo.vn/v3/vi/docs/payment/onboarding/test-instructions/ Link app test QR CODE MOMO
    @PostMapping("/secure/transactions/pay/{transactionId}")
    public ResponseEntity<?> payTransaction(@PathVariable("transactionId") String transactionId) {
        try {
            PaymentResponse response = transactionService.payTransaction(transactionId);
            return ResponseEntity.ok(Map.of("payUrl", response.getPayUrl()));
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(500).body("Server Error: " + ex.getMessage());
        }
    }

    // Tạo transaction
    @PostMapping("/secure/transactions/create-multiple")
    public ResponseEntity<?> createMultipleTransactions(@RequestBody List<TransactionDTO> data) {
        try {
            List<Transaction> created = transactionService.createTransactions(data);
            return ResponseEntity.ok(created);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(500).body("Server Error: " + ex.getMessage());
        }
    }

    // Xử lí upload img momo - update trans
    @PatchMapping(path = "/secure/pay-img/transactions/{transactionId}", consumes = MediaType.MULTIPART_FORM_DATA)
    public ResponseEntity<?> updateTransaction(@PathVariable("transactionId") String transactionId,
            @RequestParam Map<String, String> updates,
            @RequestParam(value = "momoImage", required = false) MultipartFile momoImage) {
        try {
            Transaction updated = transactionService.updateTransactionImage(transactionId, updates, momoImage);
            return ResponseEntity.ok(updated);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(500).body("Server Error: " + ex.getMessage());
        }
    }

    // Nhận notify từ MoMo (IPN) - update trans status
    @PostMapping("/momo/callback")
    public ResponseEntity<?> momoCallback(@RequestBody Map<String, String> payload) {
        try {
            return ResponseEntity.ok(transactionService.processMomoIPN(payload));
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(500).body("Server Error: " + ex.getMessage());
        }
    }

    @PatchMapping("/secure/transactions/update-status/{transactionId}")
    public ResponseEntity<?> updateStatusTransaction(@PathVariable("transactionId") String transactionId,
            @RequestParam("status") String status) {
        try {
            return ResponseEntity.ok(transactionService.updateTransactionStatus(transactionId, status));
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(500).body("Server Error: " + ex.getMessage());
        }
    }

    @GetMapping("/secure/transactions-by-apartment/{apartmentId}")
    public ResponseEntity<?> getTransactionsByApartment(@PathVariable("apartmentId") String apartmentId,
            @RequestParam Map<String, String> params) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bạn chưa đăng nhập!");
        }

        String username = auth.getName();
        User user = userService.getUserByUsername(username);
        params.put("userId", String.valueOf(user.getId()));

        try {
            List<Transaction> transactions = transactionService.getTransactionsByApartment(apartmentId, params);
            return ResponseEntity.ok(transactions);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(500).body("Server Error: " + ex.getMessage());
        }
    }
}
