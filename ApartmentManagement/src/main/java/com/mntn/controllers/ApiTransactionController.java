package com.mntn.controllers;

import com.mntn.configs.momo.Environment;
import com.mntn.enums.RequestType;
import com.mntn.pojo.Transaction;
import com.mntn.pojo.User;
import com.mntn.pojo.momo.PaymentResponse;
import com.mntn.services.TransactionService;
import com.mntn.services.UserService;
import com.mntn.services.impl.CreateOrderMomo;
import com.mntn.utils.momo.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
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

    
    // Cách test: gõ http://localhost:8080/ApartmentManagement/api/transactions/<transactionId>
    // respone "payUrl" : "https://......" -> coppy paste and search
    @GetMapping("/transactions/{transactionId}")
    public ResponseEntity<?> getTrans(@PathVariable("transactionId") String transactionId) {
        try {
            if (transactionId == null || transactionId.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("transactionId không hợp lệ!");
            }

            PaymentResponse response = transactionService.testMomoApi(transactionId);
            if (response == null || response.getPayUrl() == null || response.getPayUrl().isEmpty()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Không thể tạo URL thanh toán MoMo!");
            }

            return ResponseEntity.ok(new PayUrlResponse(response.getPayUrl()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lỗi: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi hệ thống: " + ex.getMessage());
        }
    }

    private static class PayUrlResponse {

        private final String payUrl;

        public PayUrlResponse(String payUrl) {
            this.payUrl = payUrl;
        }

        public String getPayUrl() {
            return payUrl;
        }
    }

}
