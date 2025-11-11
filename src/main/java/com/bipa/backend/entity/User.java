package com.bipa.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Long kakaoId;

    private String nickname;        // 사용자 닉네임
    private String email;           // (추가) 카카오 계정 이메일
    private String profileImage;    // (추가) 프로필 이미지 URL

    private LocalDateTime connectedAt;  // 카카오 연결 시간
    private LocalDateTime createdAt;    // 가입 일시

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserCharacter character;
}
