package com.mntn.repositories.impl;

import com.mntn.pojo.User;
import com.mntn.pojo.VehicleAccess;
import com.mntn.repositories.StatsRepository;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class StatsRepositoryImpl implements StatsRepository {
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Object[]> statsVehicleByStatus() {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);

        Root<VehicleAccess> root = query.from(VehicleAccess.class);

        query.multiselect(root.get("status"), cb.count(root));
        query.groupBy(root.get("status"));

        Query q = session.createQuery(query);
        return q.getResultList();
    }

    @Override
    public List<Object[]> statsActiveUsers() {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);

        Root<User> root = query.from(User.class);

        query.multiselect(root.get("isActive"), cb.count(root));
        query.groupBy(root.get("isActive"));

        Query q = session.createQuery(query);
        return q.getResultList();
    }

}
