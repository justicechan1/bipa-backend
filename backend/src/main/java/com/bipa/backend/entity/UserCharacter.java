package com.bipa.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
@Entity
@Table(name = "user_characters")
@Getter
@Setter
@NoArgsConstructor
public class UserCharacter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  //캐릭터 고유 ID

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;  //사용자 카카오 고유 ID

    private int level = 1;  //캐릭터 레밸
    private int exp = 0;    //캐릭터 경험치
    private int money = 0;  //보유 코인
    private int hungryGauge = 100;  //배고픔 수치(0~100)

    private int maxActopus = 0;  //미니게임 최소 점수
    private int maxFig = 0;
    private int maxYudal = 0;
    private int maxFish = 0;

    private LocalDateTime updatedAt;  //업데이트 시간
}


