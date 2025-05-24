package com.mntn.repositories.impl;

import com.mntn.pojo.Apartment;
import com.mntn.pojo.User;
import com.mntn.repositories.ApartmentRepository;
import com.mntn.services.UserService;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class ApartmentRepositoryImpl implements ApartmentRepository {
    @Autowired
    private LocalSessionFactoryBean factory;

    @Autowired
    private UserService userService;

    @Override
    public List<Apartment> getListApartmentByUserId(String userId) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM Apartment a WHERE a.currentOwnerId = :userId", Apartment.class);
        User u = userService.getUserById(userId);
        q.setParameter("userId", u);
        return q.getResultList();
    }

    @Override
    public List<Apartment> getListApartmentByBlock(String block) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM Apartment a WHERE a.block = :block ORDER BY CAST(a.floor AS int), CAST(a.number AS int)", Apartment.class);
        q.setParameter("block", block);
        return q.getResultList();
    }

    @Override
    public boolean checkExistApartment(String block, String floor, String number) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("SELECT COUNT(a) FROM Apartment a WHERE a.block = :block AND a.floor = :floor AND a.number = :number", Long.class);
        q.setParameter("block", block);
        q.setParameter("floor", floor);
        q.setParameter("number", number);
        return (Long) q.getSingleResult() > 0;
    }

    @Override
    public Apartment addApartment(Apartment a) {
        Session s = this.factory.getObject().getCurrentSession();
        s.persist(a);
        s.flush();
        return a;
    }

    @Override
    public Apartment updateApartment(Apartment a) {
        Session s = this.factory.getObject().getCurrentSession();
        s.update(a);
        s.flush();
        return a;
    }

    @Override
    public void deleteApartment(String id) {
        Session s = this.factory.getObject().getCurrentSession();
        Apartment apt = this.getApartmentById(id);
        s.remove(apt);
    }

    @Override
    public Apartment getApartmentById(String id) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("Apartment.findById", Apartment.class);
        q.setParameter("id", id);
        return (Apartment) q.getSingleResult();
    }
}
