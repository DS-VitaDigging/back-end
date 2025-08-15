package com.example.VitaDigging.controller;

import com.example.VitaDigging.dto.ChatRequestDto;
import com.example.VitaDigging.entity.Survey;
import com.example.VitaDigging.repository.SurveyRepository;
import com.example.VitaDigging.security.CustomUser;
import com.example.VitaDigging.service.ChatGptService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatGptService chatGptService;
    private final SurveyRepository surveyRepository;

    @PostMapping
    public String chat(@RequestBody ChatRequestDto requestDto,
                       @AuthenticationPrincipal CustomUser user) throws Exception {

        // 로그인한 사용자 ID 가져오기
        String userId = user.getUserId();

        // DB에서 가장 최근 신체 정보 조회
        Survey survey = surveyRepository.findFirstByUserIdOrderByIdDesc(userId)
                .orElseThrow(() -> new IllegalStateException(
                        "신체 정보가 없습니다. 먼저 /api/surveys/body로 입력해주세요."
                ));

        // GPT 호출
        return chatGptService.ask(requestDto.getMessages(), survey.getHeight(), survey.getWeight());
    }
}


