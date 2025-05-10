package com.mntn.services;

import com.mntn.pojo.Transaction;
import com.mntn.repositories.TransactionRepository;
import java.util.List;

public interface TransactionService {

    List<Transaction> getTransactionsByUserIdStatusAndCategory(String userId, String status, String categoryId);

}
