package com.mntn.repositories.impl;

import com.mntn.pojo.Transaction;
import com.mntn.repositories.TransactionRepository;
import jakarta.persistence.Query;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class TransactionRepositoryImpl implements TransactionRepository {

    private static final int PAGE_SIZE = 3;
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Transaction> getTransactions(Map<String, String> params) {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Transaction> cq = cb.createQuery(Transaction.class);
        Root<Transaction> root = cq.from(Transaction.class);

        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();

            // Lọc
            if (params.get("userId") != null) {
                predicates.add(cb.equal(root.get("userId").get("id"), params.get("userId")));
            }

            if (params.get("status") != null && !params.get("status").isEmpty()) {
                predicates.add(cb.equal(root.get("status"), params.get("status")));
            }

            if (params.get("categoryId") != null && !params.get("categoryId").isEmpty()) {
                predicates.add(cb.equal(root.get("categoryId").get("id"), params.get("categoryId")));
            }

            if (params.get("methodId") != null && !params.get("methodId").isEmpty()) {
                predicates.add(cb.equal(root.get("methodId").get("id"), params.get("methodId")));
            }

            if (params.get("fromDate") != null && !params.get("fromDate").isEmpty()) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdDate"), java.sql.Date.valueOf(params.get("fromDate"))));
            }

            if (params.get("toDate") != null && !params.get("toDate").isEmpty()) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdDate"), java.sql.Date.valueOf(params.get("toDate"))));
            }

            cq.where(predicates.toArray(new Predicate[0]));
            cq.orderBy(
                    cb.desc(root.get("createdDate")),
                    cb.desc(root.get("id"))
            );
        }
        Query query = session.createQuery(cq);

        // Phân trang
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int start = (page - 1) * PAGE_SIZE;
        query.setFirstResult(start);
        query.setMaxResults(PAGE_SIZE);

        return query.getResultList();
    }

}
