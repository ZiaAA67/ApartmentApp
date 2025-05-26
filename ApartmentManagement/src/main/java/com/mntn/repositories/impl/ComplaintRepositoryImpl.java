package com.mntn.repositories.impl;

import com.mntn.pojo.Complaint;
import com.mntn.repositories.ComplaintRepository;
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
public class ComplaintRepositoryImpl implements ComplaintRepository {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Complaint getComplaintById(String complaintId) {
        Session session = this.factory.getObject().getCurrentSession();
        Query q = session.createNamedQuery("Complaint.findById", Complaint.class);
        q.setParameter("id", complaintId);
        return (Complaint) q.getSingleResult();
    }

    @Override
    public List<Complaint> getComplaints(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<Complaint> cq = cb.createQuery(Complaint.class);
        Root<Complaint> root = cq.from(Complaint.class);

        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();
            if (params.get("userId") != null) {
                predicates.add(cb.equal(root.get("userId").get("id"), params.get("userId")));
            }

            if (params.get("status") != null && !params.get("status").isEmpty()) {
                predicates.add(cb.equal(root.get("status"), params.get("status")));
            }

            cq.where(predicates.toArray(new Predicate[0]));
            cq.orderBy(
                    cb.desc(root.get("dateSubmitted")),
                    cb.desc(root.get("id"))
            );
        }

        Query query = s.createQuery(cq);

        // Phân trang
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int start = (page - 1) * PAGE_SIZE;
        query.setFirstResult(start);
        query.setMaxResults(PAGE_SIZE);

        return query.getResultList();
    }

    @Override
    public Complaint createComplaint(Complaint c) {
        Session s = this.factory.getObject().getCurrentSession();
        s.persist(c);
        s.flush();
        return c;
    }

    @Override
    public Complaint updateComplaint(Complaint c) {
        Session s = this.factory.getObject().getCurrentSession();
        s.update(c);
        s.flush();
        return c;
    }
}
