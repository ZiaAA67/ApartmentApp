package com.mntn.services;

import com.mntn.pojo.Transaction;
import java.util.List;
import java.util.Map;

public interface TransactionService {

    List<Transaction> getTransactions(Map<String, String> params);
}
