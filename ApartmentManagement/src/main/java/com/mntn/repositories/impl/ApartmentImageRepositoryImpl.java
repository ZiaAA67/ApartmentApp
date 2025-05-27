package com.mntn.repositories.impl;

import com.mntn.pojo.ApartmentImage;
import com.mntn.repositories.ApartmentImageRepository;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class ApartmentImageRepositoryImpl implements ApartmentImageRepository {
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public ApartmentImage addImage(ApartmentImage img) {
        Session s = this.factory.getObject().getCurrentSession();
        s.persist(img);
        s.flush();
        return img;
    }

    @Override
    public List<ApartmentImage> getListImages(String aptId) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM ApartmentImage WHERE apartmentId = :aptId", ApartmentImage.class);
        q.setParameter("aptId", aptId);
        return q.getResultList();
    }
}
