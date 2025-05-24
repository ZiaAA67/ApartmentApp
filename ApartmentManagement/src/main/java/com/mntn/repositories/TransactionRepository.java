package com.mntn.repositories;

import com.mntn.pojo.Transaction;
import java.util.List;
import java.util.Map;

public interface TransactionRepository {

    List<Transaction> getTransactions(Map<String, String> params);

    List<Transaction> getTransactionsByApartmentId(String apartmentId);

    Transaction getTransactionById(String transactionId);
}
