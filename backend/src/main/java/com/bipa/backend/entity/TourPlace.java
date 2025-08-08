package com.bipa.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
        name = "tour_places",
        uniqueConstraints = @UniqueConstraint(columnNames = "place_name")
)
public class TourPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  //장소 고유 ID

    @Column(name = "place_name", nullable = false, unique = true)
    private String placeName;  //장소명

    @Column(nullable = false)
    private String category;  //카테고리

    @Column(nullable = false)
    private String address;  //주소

    @Column(name = "x_cord", nullable = false)
    private Double xCord;  //경도

    @Column(name = "y_cord", nullable = false)
    private Double yCord;  //위도

    @Column(nullable = false)
    private String division;  //카테고리 분류
}
