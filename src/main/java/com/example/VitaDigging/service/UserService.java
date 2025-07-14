package com.example.VitaDigging.service;

import com.example.VitaDigging.dto.*;
import com.example.VitaDigging.entity.UserEntity;
import com.example.VitaDigging.exception.DuplicateIDException;
import com.example.VitaDigging.exception.IncorrectLoginException;
import com.example.VitaDigging.repository.UserRepository;
import com.example.VitaDigging.security.JwtTokenProvider;
import com.example.VitaDigging.util.AgeCalculator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));

        // 생년월일
        LocalDate birth = user.getBirth();  // 예: 1999-07-14
        // 나이 계산
        String age = AgeCalculator.calculateKoreanAndInternationalAge(
                birth.getYear(), birth.getMonthValue(), birth.getDayOfMonth()
        );

        return new ProfilesResponseDto(
                user.getName(),
                age,
                user.getId(),
                user.getEmail()
        );
    }

    // 닉네임 수정
    public void updateName(String id, NameUpdateRequestDto request) {
        UserEntity user = userRepository.findUserById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        if (request.getName() != null && !request.getName().isEmpty()) {
            user.setName(request.getName());
        }

        userRepository.save(user);  // 변경사항 저장
    }

    // 비밀번호 수정
    public void updatePassword(String id, PasswordUpdateRequestDto request) {
        UserEntity user = userRepository.findUserById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        String password = request.getPassword();
        String passwordConfirm = request.getPasswordConfirm();

        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("비밀번호를 입력해주세요.");
        }

        if (!password.equals(passwordConfirm)) {
            throw new IllegalArgumentException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);

        userRepository.save(user);
    }


}
