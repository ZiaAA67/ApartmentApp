package com.mntn.controllers;

import com.mntn.pojo.Survey;
import com.mntn.pojo.SurveyOption;
import com.mntn.services.SurveyOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class ApiSurveyOptionController {
    @Autowired
    private SurveyOptionService optionService;

    @GetMapping("/secure/survey/{surveyId}/option")
    public ResponseEntity<List<SurveyOption>> getListSurveyOption(@PathVariable(value = "surveyId") String surveyId) {
        return new ResponseEntity<>(this.optionService.getListOption(surveyId), HttpStatus.OK);
    }

    @PostMapping("/secure/survey/{surveyId}/option")
    public ResponseEntity<Object> createSurveyOption(@PathVariable(value = "surveyId") String surveyId,
                                                     @RequestBody Map<String, String> params) {
        try {
            return new ResponseEntity<>(this.optionService.createOption(surveyId, params), HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }
}
