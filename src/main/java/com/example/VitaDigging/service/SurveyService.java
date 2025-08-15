package com.example.VitaDigging.service;

import com.example.VitaDigging.dto.SurveyRequestDto;
import com.example.VitaDigging.entity.Survey;
import com.example.VitaDigging.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository surveyRepository;

    public void saveSurvey(String userId, SurveyRequestDto dto) {
        Survey survey = Survey.builder()
                .userId(userId)
                .height(dto.getHeight())
                .weight(dto.getWeight())
                .build();

        surveyRepository.save(survey);
    }
}
