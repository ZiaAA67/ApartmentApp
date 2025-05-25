package com.mntn.services.impl;

import com.mntn.pojo.Survey;
import com.mntn.pojo.User;
import com.mntn.repositories.SurveyRepository;
import com.mntn.repositories.UserRepository;
import com.mntn.services.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class SurveyServiceImpl implements SurveyService {
    @Autowired
    private SurveyRepository surveyRepo;

    @Autowired
    private UserRepository userRepo;

    @Override
    public Survey createSurvey(Map<String, String> params) {
        Survey sv = new Survey();
        sv.setId(UUID.randomUUID().toString());
        sv.setActive(true);
        sv.setCreatedDate(new Date());

        String title = params.get("title");
        if (title == null)
            throw new IllegalArgumentException("Tiêu đề khảo sát chưa được cung cấp!");
        sv.setTitle(title);

        sv.setDescription(params.getOrDefault("description", null));

        String userId = params.get("userId");
        if (userId == null || userId.isEmpty())
            throw new IllegalArgumentException("Người tạo khảo sát không tồn tại!");
        User user = userRepo.getUserById(userId);
        sv.setUserId(user);

        return this.surveyRepo.createSurvey(sv);
    }

    @Override
    public Survey getSurveyById(String id) {
        return this.surveyRepo.getSurveyById(id);
    }

    @Override
    public List<Survey> getListSurvey() {
        return this.surveyRepo.getListSurvey();
    }
}
