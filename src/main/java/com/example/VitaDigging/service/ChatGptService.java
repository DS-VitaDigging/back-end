package com.example.VitaDigging.service;

import com.example.VitaDigging.dto.MessageDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;

@Service
public class ChatGptService {

    @Value("${openai.api-key}")
    private String apiKey;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    private final ObjectMapper objectMapper = new ObjectMapper();


    public String ask(List<MessageDto> messages) throws Exception {
        List<Map<String, String>> chatMessages = new ArrayList<>();

        for (MessageDto m : messages) {
            chatMessages.add(Map.of("role", m.getRole(), "content", m.getContent()));
        }

        // system 프롬프트를 제일 앞에 넣음
        chatMessages.add(0, Map.of(
                "role", "system",
                "content", "당신은 건강 상태를 바탕으로 영양제를 추천해주는 AI입니다. 순차적으로 질환 → 식습관 → 운동량 → 복용약 → 원하는 카테고리를 질문하세요."
        ));

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4o",
                "messages", chatMessages
        );

        String body = objectMapper.writeValueAsString(requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

}
