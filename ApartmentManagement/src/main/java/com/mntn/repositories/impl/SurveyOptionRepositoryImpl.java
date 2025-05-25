package com.mntn.repositories.impl;

import com.mntn.pojo.Survey;
import com.mntn.pojo.SurveyOption;
import com.mntn.repositories.SurveyOptionRepository;
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
public class SurveyOptionRepositoryImpl implements SurveyOptionRepository {
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public SurveyOption createOption(SurveyOption opt) {
        Session s = this.factory.getObject().getCurrentSession();
        s.persist(opt);
        s.flush();
        return opt;
    }

    @Override
    public SurveyOption getOptionById(String id) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("SurveyOption.findById", SurveyOption.class);
        q.setParameter("id", id);
        return (SurveyOption) q.getSingleResult();
    }

    @Override
    public List<SurveyOption> getListOption(String surveyId) {
        Session s = this.factory.getObject().getCurrentSession();
        Query<Object[]> query = s.createQuery(
                "SELECT opt, COUNT(resp.id) " +
                        "FROM SurveyOption opt " +
                        "LEFT JOIN SurveyResponse resp ON opt.id = resp.optionId " +
                        "WHERE opt.surveyId = :surveyId " +
                        "GROUP BY opt.id",
                Object[].class
        );
        query.setParameter("surveyId", surveyId);
        List<Object[]> results = query.getResultList();

        return results.stream().map(result -> {
            SurveyOption option = (SurveyOption) result[0];
            Long voteCount = (Long) result[1];
            option.setVotes(voteCount.intValue());
            return option;
        }).collect(Collectors.toList());
    }
}
