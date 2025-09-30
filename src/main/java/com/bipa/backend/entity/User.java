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
    private Long id;  //사용자 카카오 고유 ID

    private String nickname;  //사용자 닉네임

    private LocalDateTime connectedAt;  //카카오 연결 시간

    private LocalDateTime createdAt;  //가입 일시

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserCharacter character;
}
