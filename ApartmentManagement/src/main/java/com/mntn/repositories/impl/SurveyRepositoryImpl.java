package com.mntn.repositories.impl;

import com.mntn.pojo.Survey;
import com.mntn.repositories.SurveyRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@Transactional
public class SurveyRepositoryImpl implements SurveyRepository {
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Survey createSurvey(Survey sv) {
        Session s = this.factory.getObject().getCurrentSession();
        s.persist(sv);
        s.flush();
        return sv;
    }

    @Override
    public Survey getSurveyById(String id) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("Survey.findById", Survey.class);
        q.setParameter("id", id);
        return (Survey) q.getSingleResult();
    }

    @Override
    public List<Survey> getListSurvey() {
        Session s = this.factory.getObject().getCurrentSession();
        Query<Object[]> query = s.createQuery(
                "SELECT s, COUNT(resp.id) " +
                        "FROM Survey s " +
                        "LEFT JOIN SurveyResponse resp ON s.id = resp.surveyId " +
                        "GROUP BY s.id " +
                        "ORDER BY s.createdDate DESC",
                Object[].class
        );
        List<Object[]> results = query.getResultList();

        return results.stream().map(result -> {
            Survey survey = (Survey) result[0];
            Long voteCount = (Long) result[1];
            survey.setTotalVotes(voteCount.intValue());
            return survey;
        }).collect(Collectors.toList());
    }
}
