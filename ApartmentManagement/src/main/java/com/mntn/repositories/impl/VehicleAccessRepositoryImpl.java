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

    @Override
    public PaginatedResponse<VehicleAccess> getListVehicleAccess(Map<String, String> params) {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();

        CriteriaQuery<VehicleAccess> cq = cb.createQuery(VehicleAccess.class);
        Root<VehicleAccess> root = cq.from(VehicleAccess.class);

        List<Predicate> predicates = buildPredicates(cb, root, params);
        if (!predicates.isEmpty()) {
            cq.where(predicates.toArray(new Predicate[0]));
        }

        cq.orderBy(cb.asc(root.get("createdDate")));

        Query dataQuery = session.createQuery(cq);
        int page = params != null && params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;
        int start = (page - 1) * PAGE_SIZE;
        dataQuery.setFirstResult(start);
        dataQuery.setMaxResults(PAGE_SIZE);

        List<VehicleAccess> data = dataQuery.getResultList();

        // ===== Query count =====
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<VehicleAccess> countRoot = countQuery.from(VehicleAccess.class);

        List<Predicate> countPredicates = buildPredicates(cb, countRoot, params);
        if (!countPredicates.isEmpty()) {
            countQuery.where(countPredicates.toArray(new Predicate[0]));
        }
        countQuery.select(cb.count(countRoot));

        Long totalItems = session.createQuery(countQuery).getSingleResult();
        int totalPages = (int) Math.ceil((double) totalItems / PAGE_SIZE);

        return new PaginatedResponse<>(data, totalItems, totalPages, page);
    }

    private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<VehicleAccess> root, Map<String, String> params) {
        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                Join<VehicleAccess, User> userJoin = root.join("userId", JoinType.INNER);
                Predicate nameLike = cb.like(
                        cb.lower(cb.concat(cb.concat(userJoin.get("firstName"), " "), userJoin.get("lastName"))),
                        "%" + kw.toLowerCase() + "%"
                );
                predicates.add(nameLike);
            }

            String status = params.get("status");
            if (status != null && !status.isEmpty()) {
                predicates.add(cb.equal(root.get("status"), status));
            }
        }

        return predicates;
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

    @Override
    public VehicleAccess updateVehicleAccess(VehicleAccess v) {
        Session s = this.factory.getObject().getCurrentSession();

        s.update(v);
        s.flush();

        return v;
    }

    @Override
    public VehicleAccess getVehicleAccessById(String id) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("VehicleAccess.findById", VehicleAccess.class);
        q.setParameter("id", id);

        return (VehicleAccess) q.getSingleResult();
    }
}
