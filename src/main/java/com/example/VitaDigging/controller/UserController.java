package com.example.VitaDigging.controller;

import com.example.VitaDigging.dto.*;
import com.example.VitaDigging.exception.DuplicateIDException;
import com.example.VitaDigging.security.JwtTokenProvider;
import com.example.VitaDigging.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    // 아이디 중복 체크
    @GetMapping("/checkId")
    public ResponseEntity<ResponseDto<Integer>> checkIdDuplicate(@RequestParam String id) {
        boolean isDuplicate = userService.checkId(id);
        ResponseDto<Integer> response;

        if(isDuplicate){
            throw new DuplicateIDException("사용 중인 아이디");
        } else {
            response = new ResponseDto<>(200, true,"사용 가능한 아이디", 1);
        }

        return ResponseEntity.ok(response);
    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<ResponseDto<Integer>> signup(@Valid @RequestBody SignupRequestDto request){
        userService.signup(request);

        ResponseDto<Integer> response = new ResponseDto<>(200, true, "회원가입 성공", 1);

        return ResponseEntity.ok().body(response);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<ResponseDto<TokenResponseDto>> login(@Valid @RequestBody LoginRequestDto request){
        TokenResponseDto token = userService.login(request);

        ResponseDto<TokenResponseDto> response = new ResponseDto<>(200, true, "로그인 성공", token);

        return ResponseEntity.ok(response);
    }

    // 프로필 조회
    @GetMapping("/profiles")
    public ResponseEntity<ResponseDto<ProfilesResponseDto>> getUserInfo(@RequestHeader("Authorization") String token){
        Authentication authentication = jwtTokenProvider.getAuthentication(token.replace("Bearer ", ""));
        String userId = authentication.getName();

        ProfilesResponseDto userInfo = userService.getUserInfo(userId);;

        ResponseDto<ProfilesResponseDto> response = new ResponseDto<>(200, true, "프로필 조회 성공", userInfo);

        return ResponseEntity.ok(response);
    }

    // 프로필 수정
    @PutMapping("/profiles")
    public ResponseEntity<ResponseDto<String>> updateProfile(
            @RequestHeader("Authorization") String token,
            @RequestBody ProfileUpdateRequestDto request) {

        Authentication authentication = jwtTokenProvider.getAuthentication(token.replace("Bearer ", ""));
        String userId = authentication.getName();

        userService.updateProfile(userId, request);

        ResponseDto<String> response = new ResponseDto<>(200, true, "프로필 수정 성공", "ok");
        return ResponseEntity.ok(response);
    }
}
