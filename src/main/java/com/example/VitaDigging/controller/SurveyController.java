package com.example.VitaDigging.controller;

import com.example.VitaDigging.dto.SurveyRequestDto;
import com.example.VitaDigging.security.CustomUser;
import com.example.VitaDigging.service.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/surveys/body")
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyService surveyService;

    @PostMapping
    public ResponseEntity<Map<String, String>> saveSurvey(@AuthenticationPrincipal CustomUser user,
                                                          @RequestBody SurveyRequestDto dto) {
        if (user == null) {
            throw new RuntimeException("로그인 정보가 없습니다.");
        }

        surveyService.saveSurvey(user.getUserId(), dto);
        return ResponseEntity.ok(Map.of("message", "신체 정보가 저장되었습니다."));
    }
}

