package com.example.VitaDigging.controller;

import com.example.VitaDigging.dto.ChatRequestDto;
import com.example.VitaDigging.service.ChatGptService;
import com.example.VitaDigging.service.RecommendService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatGptService chatGptService;
    private final RecommendService recommendService;

    public ChatController(ChatGptService chatGptService, RecommendService recommendService) {
        this.chatGptService = chatGptService;
        this.recommendService = recommendService;
    }


    // 1. 일반 챗봇 기능 (GPT 호출만)
    @PostMapping
    public String ask(@RequestBody ChatRequestDto requestDto) throws Exception {
        return chatGptService.ask(requestDto.getMessages());
    }


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
