package com.example.VitaDigging.dto;

import lombok.Getter;

@Getter
public class ProfileUpdateRequestDto {
    private String name;       // 변경할 닉네임
    private String password;   // 변경할 비밀번호
}
