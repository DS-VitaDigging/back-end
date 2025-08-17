package com.example.VitaDigging.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
public class RecommendService {
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();
    private final ObjectMapper objectMapper = new ObjectMapper();

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

        return (List<Map<String, Object>>) flaskResponseMap.get("recommendations");
    }
}
