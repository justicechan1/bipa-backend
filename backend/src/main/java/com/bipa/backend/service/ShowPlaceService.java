package com.bipa.backend.service;

import com.bipa.backend.dto.place.PlaceVisitDto;
import com.bipa.backend.entity.CafePlace;
import com.bipa.backend.entity.RestaurantPlace;
import com.bipa.backend.entity.UserBook;
import com.bipa.backend.repository.CafePlaceRepository;
import com.bipa.backend.repository.RestaurantPlaceRepository;
import com.bipa.backend.repository.UserBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 특정 division(카페/음식점)에 속한 전체 장소 목록을 조회하고,
 * 해당 사용자가 방문한 적 있는 장소인지 여부를 함께 표시하는 서비스 클래스.
 */
@Service
@RequiredArgsConstructor
public class ShowPlaceService {

    private final CafePlaceRepository cafeRepo;
    private final RestaurantPlaceRepository restRepo;
    private final UserBookRepository userBookRepo;

    public List<PlaceVisitDto> getPlacesWithVisit(Long userId, String divisionRaw) {
        String division = normalizeDivision(divisionRaw);
        // 카페 or 음식점 분류
        if ("cafe".equals(division)) {
            List<CafePlace> places = cafeRepo.findAll();
            Set<Long> visited = visitedSet(userId, division);
            return places.stream()
                    .map(p -> new PlaceVisitDto(p.getPlaceName(), visited.contains(p.getId())))
                    .collect(Collectors.toList());

        } else if ("restaurant".equals(division)) {
            List<RestaurantPlace> places = restRepo.findAll();
            Set<Long> visited = visitedSet(userId, division);
            return places.stream()
                    .map(p -> new PlaceVisitDto(p.getPlaceName(), visited.contains(p.getId())))
                    .collect(Collectors.toList());
        }

        throw new IllegalArgumentException("Unsupported division: " + divisionRaw);
    }

    private String normalizeDivision(String d) {
        if (d == null) return "";
        d = d.trim().toLowerCase();

        // 한글 → 영어 변환
        if (d.equals("카페")) return "cafe";
        if (d.equals("음식점")) return "restaurant";

        // 영어 키워드 처리
        if (d.startsWith("cafe")) return "cafe";
        if (d.startsWith("rest")) return "restaurant";

        return d;
    }

    private Set<Long> visitedSet(Long userId, String division) {
        List<UserBook> books = userBookRepo.findByUserIdAndDivision(userId, division);
        return books.stream().map(UserBook::getPlaceId).collect(Collectors.toSet());
    }
}