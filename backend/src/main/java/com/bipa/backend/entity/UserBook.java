package com.bipa.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_book")
@Getter
@Setter
@NoArgsConstructor
public class UserBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 도감 고유 ID (PK)

    @Column(name = "user_id", nullable = false)
    private Long userId;  // 사용자 고유 ID

    @Column(name = "place_id", nullable = false)
    private Long placeId; // 장소 고유 ID (카페 or 음식점)

    @Column(nullable = false)
    private String division;  // "cafe" or "restaurant"
}
