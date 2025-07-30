package com.example.VitaDigging.dto;

import java.util.List;

public class ChatRequestDto {
    private List<MessageDto> messages;

    public ChatRequestDto() {}

    public ChatRequestDto(List<MessageDto> messages) {
        this.messages = messages;
    }

    public List<MessageDto> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageDto> messages) {
        this.messages = messages;
    }
}