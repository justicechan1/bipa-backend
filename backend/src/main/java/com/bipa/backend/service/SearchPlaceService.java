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

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
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
                .findTop50ByPlaceNameContainingIgnoreCaseOrCategoryContainingIgnoreCaseOrDivisionContainingIgnoreCase(q, q, q);
        List<RestaurantPlace> rests = restRepo
                .findTop50ByPlaceNameContainingIgnoreCaseOrCategoryContainingIgnoreCaseOrDivisionContainingIgnoreCase(q, q, q);
        List<TourPlace> tours = tourRepo
                .findTop50ByPlaceNameContainingIgnoreCaseOrCategoryContainingIgnoreCaseOrDivisionContainingIgnoreCase(q, q, q);

        // 2) place_name만 추출 + 중복 제거(순서 보존) + 정렬
        Set<String> names = new LinkedHashSet<>();
        cafes.forEach(c -> { if (c.getPlaceName() != null) names.add(c.getPlaceName()); });
        rests.forEach(r -> { if (r.getPlaceName() != null) names.add(r.getPlaceName()); });
        tours.forEach(t -> { if (t.getPlaceName() != null) names.add(t.getPlaceName()); });

        List<SearchPlaceResponse.Result> results = names.stream()
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .map(SearchPlaceResponse.Result::new)
                .collect(Collectors.toList());

        return new SearchPlaceResponse(results);
    }
}
