package com.mntn.services;

import com.mntn.pojo.Transaction;
import com.mntn.pojo.momo.PaymentResponse;
import java.util.List;
import java.util.Map;

public interface TransactionService {

    List<Transaction> getTransactions(Map<String, String> params);

    // PaymentResponse updateStatusTransaction(String transactionId) throws RuntimeException;
    PaymentResponse testMomoApi(String transactionId) throws RuntimeException;
}
