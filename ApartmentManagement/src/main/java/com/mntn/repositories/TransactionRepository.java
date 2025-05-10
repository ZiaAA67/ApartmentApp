package com.mntn.repositories;

import com.mntn.pojo.Transaction;
import java.util.List;

public interface TransactionRepository {

    List<Transaction> getTransactionsByUserIdStatusAndCategory(String userId, String status, String categoryId);
}
