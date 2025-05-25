package com.mntn.services.impl;

import com.mntn.pojo.SurveyOption;
import com.mntn.pojo.User;
import com.mntn.repositories.SurveyOptionRepository;
import com.mntn.services.SurveyOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class SurveyOptionServiceImpl implements SurveyOptionService {
    @Autowired
    private SurveyOptionRepository optionRepo;

    @Override
    public SurveyOption createOption(String surveyId, Map<String, String> params) {
        SurveyOption opt = new SurveyOption();
        opt.setId(UUID.randomUUID().toString());
        opt.setSurveyId(surveyId);

        String content = params.get("content");
        if (content == null)
            throw new IllegalArgumentException("Nội dung của lựa chọn chưa được cung cấp!");
        opt.setContent(content);

        return this.optionRepo.createOption(opt);
    }

    @Override
    public SurveyOption getOptionById(String id) {
        return this.optionRepo.getOptionById(id);
    }

    @Override
    public List<SurveyOption> getListOption(String surveyId) {
        return this.optionRepo.getListOption(surveyId);
    }
}
