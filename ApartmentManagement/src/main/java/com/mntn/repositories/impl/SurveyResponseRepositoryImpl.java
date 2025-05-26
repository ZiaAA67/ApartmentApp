package com.mntn.repositories.impl;

import com.mntn.pojo.SurveyResponse;
import com.mntn.pojo.User;
import com.mntn.pojo.VehicleAccess;
import com.mntn.repositories.SurveyResponseRepository;
import com.mntn.repositories.UserRepository;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class SurveyResponseRepositoryImpl implements SurveyResponseRepository {
    @Autowired
    private LocalSessionFactoryBean factory;

    @Autowired
    private UserRepository userRepo;

    @Override
    public SurveyResponse createResponse(SurveyResponse res) {
        Session s = this.factory.getObject().getCurrentSession();
        s.persist(res);
        s.flush();
        return res;
    }

    @Override
    public List<SurveyResponse> getListResponse(String surveyId, String optionId) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM SurveyResponse r WHERE r.surveyId = :surveyId AND r.optionId = :optionId", SurveyResponse.class);
        q.setParameter("surveyId", surveyId);
        q.setParameter("optionId", optionId);
        return q.getResultList();
    }

    @Override
    public List<SurveyResponse> getUserResponse(String userId) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM SurveyResponse r WHERE r.userId = :userId", SurveyResponse.class);
        User u = this.userRepo.getUserById(userId);
        q.setParameter("userId", u);
        return q.getResultList();
    }
}
