package com.example.VitaDigging.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupRequestDto {

    @NotBlank(message = "아이디를 입력해주세요")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "아이디는 영어, 숫자만 사용하세요.")
    private String id;

    @NotBlank(message = "비밀번호를 입력해주세요")
    // 비밀번호 정규식 주석 처리 상태 유지
    private String password;

    @NotBlank(message = "비밀번호를 확인해주세요")
    private String passwordConfirm;  // 비밀번호 확인용 필드

    @NotBlank(message = "이름(닉네임)을 입력해주세요")
    private String name;

    @NotBlank(message = "이메일을 입력해주세요")
    @Email(message = "이메일 형식이 올바르지 않습니다")
    private String email;

    @NotNull(message = "생년월일을 입력해주세요")
    private LocalDate birth;

    @NotBlank(message = "성별을 입력해주세요")
    private String gender;
}
