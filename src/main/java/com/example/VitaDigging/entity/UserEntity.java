package com.example.VitaDigging.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // nickname

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private int birth;

    @Column(nullable = false)
    private String gender;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 테스트용 생성자
    public UserEntity(String name) {
        this.name = name;
    }

    // 사용자 생성용 생성자
    public UserEntity(String name, String password, String email, int birth, String gender) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.birth = birth;
        this.gender = gender;
    }
}
