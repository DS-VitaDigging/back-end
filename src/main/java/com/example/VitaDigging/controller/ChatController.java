package com.example.VitaDigging.controller;

import com.example.VitaDigging.service.ChatGptService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatGptService chatGptService;

    public ChatController(ChatGptService chatGptService) {
        this.chatGptService = chatGptService;
    }

    @PostMapping
    public String ask(@RequestBody Map<String, String> request) throws Exception {
        String message = request.get("message");
        return chatGptService.ask(message);
    }
}

