package com.example.VitaDigging.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProfilesResponseDto {
    private String name;
    private String age; // 출력 형식: 나이(만 나이)
    private String id;
    private String email;
    private String gender;
}
