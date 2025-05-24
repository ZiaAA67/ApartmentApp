package com.mntn.services;

import com.mntn.dto.TransactionDTO;
import com.mntn.pojo.Transaction;
import com.mntn.pojo.momo.PaymentResponse;
import java.util.List;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

public interface TransactionService {

    List<Transaction> getTransactions(Map<String, String> params);

    List<Transaction> getTransactionsByAdmin(Map<String, String> params);

    PaymentResponse payTransaction(String transactionId) throws RuntimeException;

    List<Transaction> createTransactions(List<TransactionDTO> dataList);

    Transaction updateTransaction(String id, Map<String, String> updates, MultipartFile momoImage);

}
