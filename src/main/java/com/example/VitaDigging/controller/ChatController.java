package com.example.VitaDigging.controller;

import com.example.VitaDigging.dto.ChatRequestDto;
import com.example.VitaDigging.entity.Survey;
import com.example.VitaDigging.repository.SurveyRepository;
import com.example.VitaDigging.security.CustomUser;
import com.example.VitaDigging.service.ChatGptService;
import com.example.VitaDigging.service.RecommendService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatGptService chatGptService;
    private final SurveyRepository surveyRepository;
    private final RecommendService recommendService;

    // 1. 일반 챗봇 기능 (GPT 호출만)
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

    // 2. GPT 결과 기반 추천
    @PostMapping("/recommend")
    public List<Map<String, Object>> recommendFromGptResult(@RequestBody Map<String, Object> gptBody) throws Exception {
        // 1. GPT 전체 JSON에서 content 추출
        List<Map<String, Object>> choices = (List<Map<String, Object>>) gptBody.get("choices");
        if (choices == null || choices.isEmpty()) {
            throw new IllegalArgumentException("GPT choices가 비어있습니다.");
        }

        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
        String content = (String) message.get("content");
        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException("message.content가 비어있습니다.");
        }

        // 2. 코드블록 제거 및 공백 정리
        content = content.replaceAll("(?s)```.*?```", "").trim();

        // 3. vitamins 배열 추출
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> contentMap = objectMapper.readValue(content, Map.class);
        List<String> vitamins = (List<String>) contentMap.get("vitamins");
        if (vitamins == null || vitamins.isEmpty()) {
            throw new IllegalArgumentException("vitamins 배열이 필요합니다.");
        }

        // 4. 추천 서비스 호출
        return recommendService.getRecommendations(vitamins);
    }
}
