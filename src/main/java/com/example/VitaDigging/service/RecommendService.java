package com.example.VitaDigging.service;

import com.example.VitaDigging.dto.MessageDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RecommendService {
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 🆕 ChatGPT 서비스 주입
    private final ChatGptService chatGptService;

    public RecommendService(ChatGptService chatGptService) { // 🆕 생성자 주입
        this.chatGptService = chatGptService;
    }

    public List<Map<String, Object>> getRecommendations(List<String> vitamins) throws Exception {
        Map<String, Object> payload = Map.of("vitamins", vitamins);
        String payloadJson = objectMapper.writeValueAsString(payload);

        HttpRequest flaskRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:5000/recommend"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payloadJson))
                .build();

        HttpResponse<String> flaskResponse = httpClient.send(flaskRequest, HttpResponse.BodyHandlers.ofString());
        Map<String, Object> flaskResponseMap = objectMapper.readValue(flaskResponse.body(), Map.class);

        List<Map<String, Object>> recommendations =
                (List<Map<String, Object>>) flaskResponseMap.get("recommendations");

        // 2️⃣ ChatGPT로 추천 이유 생성 (MessageDto 사용)
        String reasonPrompt = buildReasonPrompt(vitamins, recommendations);
        List<MessageDto> messagesDto = new ArrayList<>();
        messagesDto.add(new MessageDto("system", "너는 영양제 추천 전문가야. 각 제품의 추천 이유를 미사여구 없이 설명해줘."));
        messagesDto.add(new MessageDto("user", reasonPrompt));

        String gptResponse = chatGptService.ask(messagesDto);
        String reasonText = extractReasonText(gptResponse);

        // 3️⃣ 추천 결과에 reason 필드 추가
        for (Map<String, Object> rec : recommendations) {
            rec.put("reason", reasonText);
        }

        return recommendations;
    }

    private String buildReasonPrompt(List<String> vitamins, List<Map<String, Object>> recommendations) {
        StringBuilder sb = new StringBuilder();
        sb.append("사용자에게 추천한 제품들의 이유를 각각 간단하게 한 문장으로 한국어로 작성해 주세요. ")
                .append("제품 이름은 제거하고, 핵심 성분과 효과 중심으로 명확하고 문장 형식으로 자연스럽게 설명하세요.\n");
        sb.append("추천된 영양소: ").append(String.join(", ", vitamins));
        return sb.toString();
    }

    private String extractReasonText(String gptResponse) throws Exception {
        Map<String, Object> gptMap = objectMapper.readValue(gptResponse, Map.class);
        List<Map<String, Object>> choices = (List<Map<String, Object>>) gptMap.get("choices");
        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
        return (String) message.get("content");
    }
}