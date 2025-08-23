package com.bipa.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cafe_places")
@Getter
@Setter
@NoArgsConstructor
public class CafePlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  //고유ID

    @Column(name = "place_name", nullable = false)
    private String placeName;  //장소명

    private String category;  //카테고리
    private String address;  //주소

    @Column(name = "x_cord")
    private Double xCord;  //경도

    @Column(name = "y_cord")
    private Double yCord;  //위도

    @Column(name = "business_hours")
    private String businessHours;  //영업시간

    @Column(columnDefinition = "json")
    private String menu;  //메뉴

    @Column(name = "image_url", columnDefinition = "json")
    private String imageUrl;  //이미지URL

    private String division;  //카테고리 분류

    @Column(name = "first_price")
    private Integer firstPrice;  //첫번째 메뉴 가격

    @Column(name = "first_menu")
    private String firstMenu;  //첫번째 메뉴 이름
}
