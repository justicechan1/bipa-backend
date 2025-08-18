package com.bipa.backend.controller;

import com.bipa.backend.dto.common.ErrorResponse;
import com.bipa.backend.dto.place.SelectPlaceRequest;
import com.bipa.backend.dto.place.SelectPlaceResponse;
import com.bipa.backend.service.SelectPlaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/place")
public class PlaceSelectController {

    private final SelectPlaceService selectPlaceService;

    // POST /place/select_place
    @PostMapping("/select_place")
    public ResponseEntity<?> selectPlace(@RequestBody @Valid SelectPlaceRequest req) {
        try {
            String name = req.getPlace().getPlaceName();
            SelectPlaceResponse resp = selectPlaceService.selectByPlaceName(name);
            return ResponseEntity.ok(resp); // {"place": { ... }} 형태로 반환
        } catch (IllegalArgumentException e) {
            // 스펙: INVALID_INPUT
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("error", "INVALID_INPUT", e.getMessage())
            ); // :contentReference[oaicite:3]{index=3}
        } catch (SelectPlaceService.PlaceNotFoundException e) {
            // 스펙: PLACE_NOT_FOUND
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ErrorResponse("error", "PLACE_NOT_FOUND", e.getMessage())
            ); // :contentReference[oaicite:4]{index=4}
        } catch (Exception e) {
            // 스펙: INTERNAT_ERROR (오탈자 유지)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ErrorResponse("error", "INTERNAT_ERROR", "Unexpected server error")
            ); // :contentReference[oaicite:5]{index=5}
        }
    }
}
