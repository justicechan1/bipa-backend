package com.bipa.backend.controller;

import com.bipa.backend.dto.common.ErrorResponse;
import com.bipa.backend.dto.place.SearchPlaceRequest;
import com.bipa.backend.dto.place.SearchPlaceResponse;
import com.bipa.backend.service.SearchPlaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/place")
public class PlaceSearchController {

    private final SearchPlaceService searchPlaceService;

    // POST /place/search_place
    @PostMapping("/search_place")
    public ResponseEntity<?> searchPlace(@RequestBody @Valid SearchPlaceRequest req) {
        try {
            SearchPlaceResponse resp = searchPlaceService.search(req.getSearch());
            // 결과가 0건이어도 200 OK + 빈 배열로 반환 (명세서에 에러 규정 없음)  :contentReference[oaicite:3]{index=3}
            return ResponseEntity.ok(resp);
        } catch (IllegalArgumentException e) {
            // 명세서 공통 에러 포맷: INVALID_INPUT  :contentReference[oaicite:4]{index=4}
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("error", "INVALID_INPUT", e.getMessage())
            );
        } catch (Exception e) {
            // 명세서 표기 오탈자 유지(INTERNAT_ERROR)  :contentReference[oaicite:5]{index=5}
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ErrorResponse("error", "INTERNAT_ERROR", "Unexpected server error")
            );
        }
    }
}
