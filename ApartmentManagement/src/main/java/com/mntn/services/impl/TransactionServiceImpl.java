package com.mntn.services.impl;

import com.mntn.configs.momo.Environment;
import com.mntn.enums.RequestType;
import com.mntn.pojo.Transaction;
import com.mntn.pojo.momo.PaymentResponse;
import com.mntn.repositories.TransactionRepository;
import com.mntn.services.TransactionService;
import com.mntn.utils.momo.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service("transactionService")
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CreateOrderMomo createOrderMomo;

    @Autowired
    private Environment environment;

    @Value("${momo.partnerCode}")
    private String partnerCode;

    @Value("${momo.returnUrl}")
    private String returnUrl;

    @Value("${momo.notifyUrl}")
    private String notifyUrl;

    @Override
    public List<Transaction> getTransactions(Map<String, String> params) {
        String userId = params.get("userId");
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("Thiếu userId!");
        }
        return transactionRepository.getTransactions(params);
    }

    @Override
    public PaymentResponse payTransaction(String transactionId) throws RuntimeException {

        if (transactionId == null || transactionId.trim().isEmpty()) {
            throw new IllegalArgumentException("transactionId không hợp lệ!");
        }

        Transaction transaction = transactionRepository.getTransactionById(transactionId);
        if (transaction == null) {
            throw new IllegalArgumentException("Không tìm thấy giao dịch!");
        }

        LogUtils.init();
        String requestId = String.valueOf(System.currentTimeMillis());
        String orderId = transactionId + "_" + System.currentTimeMillis();
        BigDecimal amount = transaction.getAmount();
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Số tiền giao dịch không hợp lệ!");
        }

        String returnURL = "https://google.com.vn";
        String notifyURL = "https://google.com.vn";

        String orderInfo = "Giao dịch " + transaction.getCategoryId().getName();
        long amountLong = amount.setScale(0, BigDecimal.ROUND_DOWN).longValue();
        try {
            PaymentResponse paymentResponse = createOrderMomo.process(
                    environment, orderId, requestId, String.valueOf(amountLong),
                    orderInfo, returnURL, notifyURL,
                    "", RequestType.CAPTURE_WALLET, Boolean.TRUE
            );
            return paymentResponse;
        } catch (Exception ex) {
            Logger.getLogger(TransactionServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalArgumentException("Lõi phần TransSerImpl!!" + ex.getMessage(), ex);
        }
    }

    @Override
    public List<Transaction> getTransactionsByApartmentId(String userId, String apartmentId) {

        if (apartmentId == null || apartmentId.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "apartmentId không hợp lệ!");
        }
        try {
            return transactionRepository.getTransactionsByApartmentId(apartmentId);
        } catch (Exception ex) {
            Logger.getLogger(TransactionServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Lỗi khi lấy giao dịch theo apartmentId: " + ex.getMessage(),
                    ex
            );
        }
    }

}
