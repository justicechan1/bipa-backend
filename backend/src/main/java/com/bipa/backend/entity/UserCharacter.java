package com.bipa.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_characters",
        uniqueConstraints = @UniqueConstraint(name="uk_user_characters_user_id", columnNames = "user_id"))
@Getter @Setter @NoArgsConstructor
public class UserCharacter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 캐릭터 고유 ID (자동증가)

    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;  // 사용자(카카오) 고유 ID

    private int level = 1;
    private int exp = 0;
    private int money = 0;

    @Column(name = "hungry_gauge")
    private int hungryGauge = 100;   // 0~100

    @Column(name = "heart_gauge")
    private int heartGauge = 100;    // 0~100

    @Column(name = "max_actopus")
    private int maxActopus = 0;

    @Column(name = "max_fig")
    private int maxFig = 0;

    @Column(name = "max_yudal")
    private int maxYudal = 0;

    @Column(name = "max_fish")
    private int maxFish = 0;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    void touchTime() {
        this.updatedAt = LocalDateTime.now();
    }
}
