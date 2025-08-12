package com.example.VitaDigging.controller;

import com.example.VitaDigging.dto.ChatRequestDto;
import com.example.VitaDigging.service.ChatGptService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatGptService chatGptService;

    public ChatController(ChatGptService chatGptService) {
        this.chatGptService = chatGptService;
    }

    @PostMapping
    public String ask(@RequestBody ChatRequestDto requestDto) throws Exception {
        return chatGptService.ask(requestDto.getMessages());
    }
}

