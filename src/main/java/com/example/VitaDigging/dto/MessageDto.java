package com.example.VitaDigging.dto;

public class MessageDto {
    private String role;
    private String content;

    // 기본 생성자 (Jackson이 필요로 함)
    public MessageDto() {}

    public MessageDto(String role, String content) {
        this.role = role;
        this.content = content;
    }

    // Getter/Setter
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}