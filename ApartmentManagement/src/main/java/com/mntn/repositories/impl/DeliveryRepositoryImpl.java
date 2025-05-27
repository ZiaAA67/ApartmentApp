package com.mntn.repositories.impl;

import com.mntn.pojo.Delivery;
import com.mntn.repositories.DeliveryRepository;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class DeliveryRepositoryImpl implements DeliveryRepository {

    private static final int PAGE_SIZE = 10;
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Delivery> getDelivery(Map<String, String> params) {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Delivery> cq = cb.createQuery(Delivery.class);
        Root<Delivery> root = cq.from(Delivery.class);

        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();

            if (params.get("apartmentId") != null && !params.get("apartmentId").isEmpty()) {
                predicates.add(cb.equal(root.get("apartmentId").get("id"), params.get("apartmentId")));
            }

            if (params.get("status") != null && !params.get("status").isEmpty()) {
                predicates.add(cb.equal(root.get("status"), params.get("status")));
            }

            cq.where(predicates.toArray(new Predicate[0]));
            cq.orderBy(
                    cb.desc(root.get("arrivedAt")),
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

    @Override
    public Delivery getDeliveryById(String deliveryId) {
        Session session = this.factory.getObject().getCurrentSession();
        Query q = session.createNamedQuery("Delivery.findById", Delivery.class);
        q.setParameter("id", deliveryId);
        return (Delivery) q.getSingleResult();
    }

    @Override
    public Delivery createDelivery(Delivery d) {
        Session s = this.factory.getObject().getCurrentSession();
        s.persist(d);
        s.flush();
        return d;
    }

    @Override
    public Delivery updateDelivery(Delivery d) {
        Session s = this.factory.getObject().getCurrentSession();
        s.update(d);
        s.flush();
        return d;
    }

}
