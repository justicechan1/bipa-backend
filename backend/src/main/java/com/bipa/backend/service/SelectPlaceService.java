package com.bipa.backend.service;

import com.bipa.backend.dto.place.SelectPlaceResponse;
import com.bipa.backend.entity.CafePlace;
import com.bipa.backend.entity.RestaurantPlace;
import com.bipa.backend.repository.CafePlaceRepository;
import com.bipa.backend.repository.RestaurantPlaceRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SelectPlaceService {

    private final CafePlaceRepository cafeRepo;
    private final RestaurantPlaceRepository restRepo;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public SelectPlaceResponse selectByPlaceName(String rawName) {
        String name = rawName == null ? "" : rawName.trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("place.place_name is required");
        }

        // 1) 카페에서 먼저 찾고, 없으면 음식점에서 찾기 (이름 대소문자 무시)
        Optional<CafePlace> cafe = cafeRepo.findTop1ByPlaceNameIgnoreCaseOrderByIdAsc(name);
        if (cafe.isPresent()) {
            return new SelectPlaceResponse(
                    new SelectPlaceResponse.Place(
                            cafe.get().getPlaceName(),
                            cafe.get().getCategory(),
                            cafe.get().getAddress(),
                            cafe.get().getBusinessHours(),
                            parseMenuArray(cafe.get().getMenu())
                    )
            );
        }

        Optional<RestaurantPlace> rest = restRepo.findTop1ByPlaceNameIgnoreCaseOrderByIdAsc(name);
        if (rest.isPresent()) {
            return new SelectPlaceResponse(
                    new SelectPlaceResponse.Place(
                            rest.get().getPlaceName(),
                            rest.get().getCategory(),
                            rest.get().getAddress(),
                            rest.get().getBusinessHours(),
                            parseMenuArray(rest.get().getMenu())
                    )
            );
        }

        // 둘 다 없으면 PLACE_NOT_FOUND는 컨트롤러에서 변환
        throw new PlaceNotFoundException("No place found with name: " + name);
    }

    private List<String> parseMenuArray(String menuJson) {
        if (menuJson == null || menuJson.isBlank()) return Collections.emptyList();
        try {
            // DB에 JSON(예: ["갈비탕","곰탕"]) 으로 저장된 전제
            return objectMapper.readValue(menuJson, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            // 혹시 파싱 실패 시 "a | b | c" 같은 파이프로 들어온 케이스 방어
            String normalized = menuJson.replace("[", "")
                    .replace("]", "")
                    .replace("\"", "")
                    .trim();
            if (normalized.isEmpty()) return Collections.emptyList();
            String[] parts = normalized.split("\\s*\\|\\s*|\\s*,\\s*");
            return java.util.Arrays.stream(parts)
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .toList();
        }
    }

    // 커스텀 예외
    public static class PlaceNotFoundException extends RuntimeException {
        public PlaceNotFoundException(String msg) { super(msg); }
    }
}
