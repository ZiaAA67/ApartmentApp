package com.mntn.services.impl;

import com.mntn.pojo.SurveyOption;
import com.mntn.pojo.SurveyResponse;
import com.mntn.pojo.User;
import com.mntn.repositories.SurveyResponseRepository;
import com.mntn.repositories.UserRepository;
import com.mntn.services.SurveyResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class SurveyResponseServiceImpl implements SurveyResponseService {
    @Autowired
    private SurveyResponseRepository resRepo;

    @Autowired
    private UserRepository userRepo;

    @Override
    public SurveyResponse createResponse(String surveyId, String optionId, Map<String, String> params) {
        SurveyResponse res = new SurveyResponse();
        res.setId(UUID.randomUUID().toString());
        res.setSurveyId(surveyId);
        res.setOptionId(optionId);
        res.setSubmittedDate(new Date());

        String userId = params.get("userId");
        if (userId == null || userId.isEmpty())
            throw new IllegalArgumentException("Người phản hồi chưa được cung cấp!");
        User user = userRepo.getUserById(userId);
        res.setUserId(user);

        return this.resRepo.createResponse(res);
    }

    @Override
    public List<SurveyResponse> getListResponse(String surveyId, String optionId) {
        return this.resRepo.getListResponse(surveyId, optionId);
    }
}
