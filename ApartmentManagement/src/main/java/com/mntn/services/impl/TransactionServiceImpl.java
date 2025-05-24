package com.mntn.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.mntn.configs.momo.Environment;
import com.mntn.dto.TransactionDTO;
import com.mntn.enums.RequestType;
import com.mntn.pojo.Apartment;
import com.mntn.pojo.Category;
import com.mntn.pojo.Transaction;
import com.mntn.pojo.momo.PaymentResponse;
import com.mntn.repositories.ApartmentRepository;
import com.mntn.repositories.CategoryRepository;
import com.mntn.repositories.TransactionRepository;
import com.mntn.services.TransactionService;
import com.mntn.utils.momo.LogUtils;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.web.multipart.MultipartFile;

@Service("transactionService")
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CreateOrderMomo createOrderMomo;

    @Autowired
    private Environment environment;

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

    @Autowired
    private ApartmentRepository apartmentRepo;

    @Autowired
    private CategoryRepository categoryRepo;

    @Override
    public List<Transaction> createTransactions(List<TransactionDTO> dataList) {
        List<Transaction> result = new ArrayList<>();

        for (TransactionDTO dto : dataList) {
            BigDecimal amount = dto.getAmount();
            String apartmentId = dto.getApartmentId();
            String categoryId = dto.getCategoryId();

            Apartment a = apartmentRepo.getApartmentById(apartmentId);
            Category c = categoryRepo.findById(categoryId);

            if (a != null && c != null) {
                Transaction t = new Transaction();
                t.setId(UUID.randomUUID().toString());
                t.setAmount(amount);
                t.setCreatedDate(new Date());
                t.setStatus("unpaid");
                t.setApartmentId(a);
                t.setCategoryId(c);
                t.setUserId(null);
                t.setMethodId(null);
                t.setImage(null);

                result.add(transactionRepository.addTransaction(t));
            }
        }
        return result;
    }

    @Override
    public List<Transaction> getTransactionsByAdmin(Map<String, String> params) {
        return transactionRepository.getTransactions(params);
    }
    @Autowired
    private Cloudinary cloudinary;

    @Override
    public Transaction updateTransaction(String id, Map<String, String> updates, MultipartFile momoImage) {
        Transaction t = transactionRepository.getTransactionById(id);

        if (updates != null) {
            updates.forEach((key, value) -> {
                switch (key) {
                    case "status" ->
                        t.setStatus("completed");
                    case "paymentDate" ->
                        t.setUpdatedDate(new Date());
                }
            });
        }

        if (momoImage != null && !momoImage.isEmpty()) {
            try {
                Map res = cloudinary.uploader().upload(momoImage.getBytes(),
                        ObjectUtils.asMap("resource_type", "auto"));
                t.setImage(res.get("secure_url").toString());
                t.setUpdatedDate(new Date());
                t.setStatus("pending");
            } catch (IOException ex) {
                Logger.getLogger(TransactionServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return transactionRepository.updateTransaction(t);
    }
}
