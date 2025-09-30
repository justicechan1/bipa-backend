package com.bipa.backend.controller;

import com.bipa.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DBCheckController {

    private final TourPlaceRepository tourPlaceRepository;
    private final CafePlaceRepository cafePlaceRepository;
    private final RestaurantPlaceRepository restaurantPlaceRepository;
    private final UserRepository userRepository;
    private final UserCharacterRepository userCharacterRepository;
    private final UserBookRepository userBookRepository;

    @GetMapping("/DBcheck")
    public String checkAllDBConnections() {
        StringBuilder result = new StringBuilder();

        // tour_places
        try {
            long count = tourPlaceRepository.count();
            result.append("✅ tour_places 연결 성공 (총 레코드 수: ").append(count).append(")\n");
        } catch (Exception e) {
            result.append("❌ tour_places 연결 실패: ").append(e.getMessage()).append("\n");
        }

        // cafe_places
        try {
            long count = cafePlaceRepository.count();
            result.append("✅ cafe_places 연결 성공 (총 레코드 수: ").append(count).append(")\n");
        } catch (Exception e) {
            result.append("❌ cafe_places 연결 실패: ").append(e.getMessage()).append("\n");
        }

        // restaurant_places
        try {
            long count = restaurantPlaceRepository.count();
            result.append("✅ restaurant_places 연결 성공 (총 레코드 수: ").append(count).append(")\n");
        } catch (Exception e) {
            result.append("❌ restaurant_places 연결 실패: ").append(e.getMessage()).append("\n");
        }

        // users
        try {
            long count = userRepository.count();
            result.append("✅ users 연결 성공 (총 레코드 수: ").append(count).append(")\n");
        } catch (Exception e) {
            result.append("❌ users 연결 실패: ").append(e.getMessage()).append("\n");
        }

        // user_characters
        try {
            long count = userCharacterRepository.count();
            result.append("✅ user_characters 연결 성공 (총 레코드 수: ").append(count).append(")\n");
        } catch (Exception e) {
            result.append("❌ user_characters 연결 실패: ").append(e.getMessage()).append("\n");
        }

        // user_book
        try {
            long count = userBookRepository.count();
            result.append("✅ user_book 연결 성공 (총 레코드 수: ").append(count).append(")\n");
        } catch (Exception e) {
            result.append("❌ user_book 연결 실패: ").append(e.getMessage()).append("\n");
        }

        return result.toString();
    }
}
