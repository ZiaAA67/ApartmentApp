package com.mntn.controllers;

import com.mntn.pojo.Survey;
import com.mntn.services.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class ApiSurveyController {
    @Autowired
    private SurveyService surveyService;

    @GetMapping("/secure/survey")
    public ResponseEntity<List<Survey>> getListSurvey() {
        return new ResponseEntity<>(this.surveyService.getListSurvey(), HttpStatus.OK);
    }

    @GetMapping("/secure/survey/{surveyId}")
    public ResponseEntity<Survey> getSurvey(@PathVariable(value = "surveyId") String surveyId) {
        return new ResponseEntity<>(this.surveyService.getSurveyById(surveyId), HttpStatus.OK);
    }

    @PostMapping("/secure/survey")
    public ResponseEntity<Object> createSurvey(@RequestBody Map<String, String> params) {
        try {
            return new ResponseEntity<>(this.surveyService.createSurvey(params), HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }
}
