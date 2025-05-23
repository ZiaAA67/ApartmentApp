package com.mntn.services;

import com.mntn.pojo.Transaction;
import com.mntn.pojo.momo.PaymentResponse;
import java.util.List;
import java.util.Map;

public interface TransactionService {

    List<Transaction> getTransactions(Map<String, String> params);

    List<Transaction> getTransactionsByApartmentId(String userId, String apartmentId);

    PaymentResponse payTransaction(String transactionId) throws RuntimeException;

}
