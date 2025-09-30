package com.bipa.backend.controller;

import com.bipa.backend.service.TourApiService;
import com.bipa.backend.dto.TourPlaceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tour")
@RequiredArgsConstructor
public class TourApiController {

    private final TourApiService tourApiService;

    @GetMapping("/test")
    public ResponseEntity<List<TourPlaceDto>> test() {
        List<TourPlaceDto> places = tourApiService.fetchPlaces();
        return ResponseEntity.ok(places);
    }

    @GetMapping("/save")
    public ResponseEntity<String> save() {
        int added = tourApiService.saveToDatabase();
        return ResponseEntity.ok("✅ 저장 완료! " + added + "개 장소 추가됨");
    }
}
