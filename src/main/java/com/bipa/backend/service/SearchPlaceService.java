package com.bipa.backend.service;

import com.bipa.backend.dto.place.SearchPlaceResponse;
import com.bipa.backend.entity.CafePlace;
import com.bipa.backend.entity.RestaurantPlace;
import com.bipa.backend.entity.TourPlace;
import com.bipa.backend.repository.CafePlaceRepository;
import com.bipa.backend.repository.RestaurantPlaceRepository;
import com.bipa.backend.repository.TourPlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchPlaceService {

    private final CafePlaceRepository cafeRepo;
    private final RestaurantPlaceRepository restRepo;
    private final TourPlaceRepository tourRepo;

    public SearchPlaceResponse search(String rawQuery) {
        String q = rawQuery == null ? "" : rawQuery.trim();
        if (q.isEmpty()) throw new IllegalArgumentException("search must not be blank");

        // 1) 세 테이블에서 검색
        List<CafePlace> cafes = cafeRepo
                .findByPlaceNameContainingIgnoreCaseOrCategoryContainingIgnoreCaseOrDivisionContainingIgnoreCase(q, q, q);
        List<RestaurantPlace> rests = restRepo
                .findByPlaceNameContainingIgnoreCaseOrCategoryContainingIgnoreCaseOrDivisionContainingIgnoreCase(q, q, q);
        List<TourPlace> tours = tourRepo
                .findByPlaceNameContainingIgnoreCaseOrCategoryContainingIgnoreCaseOrDivisionContainingIgnoreCase(q, q, q);

        // 2) place_name + image_url 같이 관리 (중복 제거, 순서 보존)
        Map<String, String> nameToImage = new LinkedHashMap<>();

        // 카페: place_name + image_url
        cafes.forEach(c -> {
            if (c.getPlaceName() != null) {
                nameToImage.putIfAbsent(c.getPlaceName(), c.getImageUrl());
            }
        });

        // 음식점: place_name + image_url
        rests.forEach(r -> {
            if (r.getPlaceName() != null) {
                nameToImage.putIfAbsent(r.getPlaceName(), r.getImageUrl());
            }
        });

        // 관광지: image 없음 → null로 저장
        tours.forEach(t -> {
            if (t.getPlaceName() != null) {
                // 이미 같은 이름이 카페/음식점에 있으면 그 이미지 유지
                nameToImage.putIfAbsent(t.getPlaceName(), null);
            }
        });

        // 3) 이름 기준 정렬 후 DTO로 변환
        List<SearchPlaceResponse.Result> results = nameToImage.keySet().stream()
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .map(name -> new SearchPlaceResponse.Result(
                        name,
                        nameToImage.get(name)   // 카페/레스토랑이면 이미지, 관광지면 null
                ))
                .collect(Collectors.toList());

        return new SearchPlaceResponse(results);
    }
}
