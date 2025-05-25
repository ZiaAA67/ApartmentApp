package com.mntn.repositories;

import com.mntn.pojo.SurveyOption;

import java.util.List;

public interface SurveyOptionRepository {
    SurveyOption createOption(SurveyOption opt);
    SurveyOption getOptionById(String id);
    List<SurveyOption> getListOption(String surveyId);
}
