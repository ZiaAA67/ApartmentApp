package com.mntn.repositories.impl;

import com.mntn.pagination.PaginatedResponse;
import com.mntn.pojo.User;
import com.mntn.pojo.VehicleAccess;
import com.mntn.repositories.VehicleAccessRepository;
import com.mntn.services.UserService;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class VehicleAccessRepositoryImpl implements VehicleAccessRepository {
    private static final int PAGE_SIZE = 10;
    @Autowired
    private LocalSessionFactoryBean factory;

    @Autowired
    private UserService userService;

//    @Override
//    public PaginatedResponse<VehicleAccess> getListVehicleAccess(Map<String, String> params) {
//        Session s = this.factory.getObject().getCurrentSession();
//
//        CriteriaBuilder b = s.getCriteriaBuilder();
//        CriteriaQuery<VehicleAccess> q = b.createQuery(VehicleAccess.class);
//        Root root = q.from(VehicleAccess.class);
//        q.select(root);
//
////        Join<Object, Object> userJoin = root.join("userId", JoinType.INNER);
//        Join<VehicleAccess, User> userJoin = root.join("userId", JoinType.INNER);
//
//        List<Predicate> predicates = new ArrayList<>();
//        if (params != null) {
//            String kw = params.get("kw");
//            if (kw != null && !kw.isEmpty()) {
//                Predicate nameLike = b.like(
//                        b.lower(b.concat(b.concat(userJoin.get("firstName"), " "), userJoin.get("lastName"))),
//                        "%" + kw.toLowerCase() + "%"
//                );
//                predicates.add(nameLike);
//            }
//
//            String status = params.get("status");
//            if (status != null && !status.isEmpty()) {
//                predicates.add(b.equal(root.get("status"), status));
//            }
//        }
//
//        q.orderBy(b.asc(root.get("createdDate")));
//        q.where(predicates.toArray(Predicate[]::new));
//
//        Query dataQuery = s.createQuery(q);
//
//        int page = 1;
//        if (params != null && params.containsKey("page")) {
//            page = Integer.parseInt(params.get("page"));
//            int start = (page - 1) * PAGE_SIZE;
//            dataQuery.setMaxResults(PAGE_SIZE);
//            dataQuery.setFirstResult(start);
//        }
//
//        List<VehicleAccess> data = dataQuery.getResultList();
//
//        CriteriaQuery<Long> countQuery = b.createQuery(Long.class);
//        Root countRoot = countQuery.from(VehicleAccess.class);
//        Join<Object, Object> countUserJoin = countRoot.join("userId", JoinType.INNER);
//        countQuery.select(b.count(countRoot));
//
//        if (!predicates.isEmpty()) {
//            countQuery.where(predicates.toArray(Predicate[]::new));
//        }
//
//        long totalItems = s.createQuery(countQuery).getSingleResult();
//        int totalPages = (int) Math.ceil((double) totalItems / PAGE_SIZE);
//
//        return new PaginatedResponse<>(data, totalItems, totalPages, page);
//    }

    @Override
    public PaginatedResponse<VehicleAccess> getListVehicleAccess(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<VehicleAccess> q = b.createQuery(VehicleAccess.class);
        Root<VehicleAccess> root = q.from(VehicleAccess.class);
        q.select(root);

        List<Predicate> predicates = new ArrayList<>();
        Join<VehicleAccess, User> userJoin = null;
        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                userJoin = root.join("userId", JoinType.INNER);
                Predicate nameLike = b.like(
                        b.lower(b.concat(b.concat(userJoin.get("firstName"), " "), userJoin.get("lastName"))),
                        "%" + kw.toLowerCase() + "%"
                );
                predicates.add(nameLike);
            }

            String status = params.get("status");
            if (status != null && !status.isEmpty()) {
                predicates.add(b.equal(root.get("status"), status));
            }
        }

        q.orderBy(b.asc(root.get("createdDate")));
        if (!predicates.isEmpty()) {
            q.where(predicates.toArray(Predicate[]::new));
        }

        Query dataQuery = s.createQuery(q);
        int page = 1;
        if (params != null && params.containsKey("page")) {
            page = Integer.parseInt(params.get("page"));
            int start = (page - 1) * PAGE_SIZE;
            dataQuery.setMaxResults(PAGE_SIZE);
            dataQuery.setFirstResult(start);
        }

        List<VehicleAccess> data = dataQuery.getResultList();

        // Truy vấn đếm
        CriteriaQuery<Long> countQuery = b.createQuery(Long.class);
        Root<VehicleAccess> countRoot = countQuery.from(VehicleAccess.class);
        countQuery.select(b.count(countRoot));

        // Đồng bộ Join cho countQuery
        if (!predicates.isEmpty() && userJoin != null) {
            Join<VehicleAccess, User> countUserJoin = countRoot.join("userId", JoinType.INNER);
            // Tạo lại Predicate cho countQuery để đảm bảo đồng bộ
            List<Predicate> countPredicates = new ArrayList<>();
            if (params != null) {
                String kw = params.get("kw");
                if (kw != null && !kw.isEmpty()) {
                    Predicate nameLike = b.like(
                            b.lower(b.concat(b.concat(countUserJoin.get("firstName"), " "), countUserJoin.get("lastName"))),
                            "%" + kw.toLowerCase() + "%"
                    );
                    countPredicates.add(nameLike);
                }

                String status = params.get("status");
                if (status != null && !status.isEmpty()) {
                    countPredicates.add(b.equal(countRoot.get("status"), status));
                }
            }
            countQuery.where(countPredicates.toArray(Predicate[]::new));
        }

        long totalItems = s.createQuery(countQuery).getSingleResult();
        int totalPages = (int) Math.ceil((double) totalItems / PAGE_SIZE);

        return new PaginatedResponse<>(data, totalItems, totalPages, page);
    }


    @Override
    public List<VehicleAccess> getListVehicleAccessByUserId(String userId) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM VehicleAccess v WHERE v.userId = :userId ORDER BY v.createdDate DESC", VehicleAccess.class);
        User u = userService.getUserById(userId);
        q.setParameter("userId", u);
        return q.getResultList();
    }

    @Override
    public VehicleAccess addVehicleAccess(VehicleAccess v) {
        Session s = this.factory.getObject().getCurrentSession();
        s.persist(v);
        s.flush();
        return v;
    }
}
