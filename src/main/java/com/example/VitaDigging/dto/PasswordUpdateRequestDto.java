package com.example.VitaDigging.dto;

import lombok.Getter;

@Getter
public class PasswordUpdateRequestDto {
    private String password;   // 변경할 비밀번호
    private String passwordConfirm; // 변경할 비밀번호 확인
}
