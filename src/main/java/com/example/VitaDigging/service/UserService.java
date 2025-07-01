package com.example.VitaDigging.service;

import com.example.VitaDigging.dto.*;
import com.example.VitaDigging.entity.UserEntity;
import com.example.VitaDigging.exception.DuplicateIDException;
import com.example.VitaDigging.exception.IncorrectLoginException;
import com.example.VitaDigging.repository.UserRepository;
import com.example.VitaDigging.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // 회원가입
//    public void signup(SignupRequestDto request) {
//        if (checkId(request.getId())) {
//            throw new DuplicateIDException("사용 중인 아이디입니다.");
//        }
//        if (!request.getPassword().equals(request.getPasswordConfirm())) {
//            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
//        }
//
//
//        String encodedPassword = passwordEncoder.encode(request.getPassword());
//        UserEntity user = new UserEntity(request.getId(), encodedPassword);
//
//        userRepository.save(user);
//    }

    public void signup(SignupRequestDto request) {
        if (checkId(request.getId())) {
            throw new DuplicateIDException("사용 중인 아이디입니다.");
        }
        if (!request.getPassword().equals(request.getPasswordConfirm())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        UserEntity user = new UserEntity(
                request.getId(),
                request.getName(),
                encodedPassword,
                request.getEmail(),
                request.getBirth(),
                request.getGender()
        );

        userRepository.save(user);
    }



    // 아이디 중복 확인
    public boolean checkId(String id) {
        return userRepository.existsUserIdById(id);
    }

    // 로그인
    public TokenResponseDto login(LoginRequestDto request) {
        UserEntity user = userRepository.findUserById(request.getId())
                .orElseThrow(() -> new IncorrectLoginException("아이디가 틀렸습니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IncorrectLoginException("비밀번호가 틀렸습니다.");
        }

        String token = jwtTokenProvider.generateToken(user.getId());
        return new TokenResponseDto(token);
    }

    // 프로필 조회
    public ProfilesResponseDto getUserInfo(String id){
        UserEntity user = userRepository.findUserById(id)
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));

        return new ProfilesResponseDto(user.getId());
    }

    // 프로필 수정
    public void updateProfile(String id, ProfileUpdateRequestDto request) {
        UserEntity user = userRepository.findUserById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        if (request.getName() != null && !request.getName().isEmpty()) {
            user.setName(request.getName());
        }

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(request.getPassword());
            user.setPassword(encodedPassword);
        }

        userRepository.save(user);  // 변경사항 저장
    }


}
