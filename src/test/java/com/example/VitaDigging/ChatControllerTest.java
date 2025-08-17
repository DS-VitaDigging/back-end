package com.example.VitaDigging;

import com.example.VitaDigging.controller.ChatController;
import com.example.VitaDigging.service.RecommendService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ChatControllerTest {

    @Mock
    private RecommendService recommendService;

    @InjectMocks
    private ChatController chatController;

    @BeforeEach
    void setUp() {
        // @Mock 과 @InjectMocks 초기화
        MockitoAnnotations.openMocks(this);
    }

    /**
     * ✅ 정상 케이스:
     * GPT 응답에 vitamins가 들어있고, RecommendService가 잘 호출되는 경우
     */
    @Test
    void recommendFromGptResult_ShouldReturnRecommendations() throws Exception {
        // given: GPT 응답 일부 (choices -> message -> content)
        Map<String, Object> message = Map.of(
                "role", "assistant",
                "content", "{ \"vitamins\": [\"철분\", \"비타민C\", \"홍삼제품\"] }"
        );
        Map<String, Object> choice = Map.of("message", message);
        Map<String, Object> gptBody = Map.of("choices", List.of(choice));

        List<String> vitamins = List.of("철분", "비타민C", "홍삼제품");

        // when: RecommendService 가짜 응답 세팅
        List<Map<String, Object>> fakeResponse = List.of(
                Map.of("name", "철분"),
                Map.of("name", "비타민C"),
                Map.of("name", "홍삼제품")
        );
        when(recommendService.getRecommendations(vitamins)).thenReturn(fakeResponse);

        // then: 컨트롤러 실행 및 결과 검증
        List<Map<String, Object>> result = chatController.recommendFromGptResult(gptBody);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("철분", result.get(0).get("name"));
        assertEquals("비타민C", result.get(1).get("name"));
        assertEquals("홍삼제품", result.get(2).get("name"));

        // RecommendService가 정확히 1번 호출됐는지 확인
        verify(recommendService, times(1)).getRecommendations(vitamins);
    }

    /**
     * ❌ 예외 케이스 1:
     * choices가 비어있을 때
     */
    @Test
    void recommendFromGptResult_ShouldThrowIfChoicesEmpty() {
        Map<String, Object> gptBody = Map.of("choices", List.of());

        Exception e = assertThrows(IllegalArgumentException.class,
                () -> chatController.recommendFromGptResult(gptBody));

        assertTrue(e.getMessage().contains("GPT choices가 비어있습니다."));
    }

    /**
     * ❌ 예외 케이스 2:
     * message.content가 비어있을 때
     */
    @Test
    void recommendFromGptResult_ShouldThrowIfContentEmpty() {
        Map<String, Object> message = Map.of("role", "assistant", "content", "");
        Map<String, Object> choice = Map.of("message", message);
        Map<String, Object> gptBody = Map.of("choices", List.of(choice));

        Exception e = assertThrows(IllegalArgumentException.class,
                () -> chatController.recommendFromGptResult(gptBody));

        assertTrue(e.getMessage().contains("message.content가 비어있습니다."));
    }
}
