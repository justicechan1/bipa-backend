package com.bipa.backend.controller;

import com.bipa.backend.dto.place.*;
import com.bipa.backend.exception.*;
import com.bipa.backend.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/place")
public class PlaceController {
    private final ShowPlaceService showPlaceService;
    private final UserBookService userBookService;

    @PostMapping("/show_place")
    public ResponseEntity<?> showPlace(@RequestBody ShowPlaceRequest req){
        if (req==null || req.getUser()==null || req.getPlace()==null)
            throw new InvalidInputException("user/place 입력이 누락되었습니다.");
        Long userId = req.getUser().getUser_id();
        String division = req.getPlace().getDivision();
        if (userId==null || division==null || division.isBlank())
            throw new InvalidInputException("user_id 또는 division이 비어 있습니다.");
        var list = showPlaceService.getPlacesWithVisit(userId, division); // 명세 포맷 그대로 반환
        return ResponseEntity.ok(Map.of("place", list)); // "place": [ {place_name, visit}, ... ]
    }

    // 2) 사용자 도감(북)에 장소 추가
    @PostMapping("/inuserbook")
    public ResponseEntity<?> inUserBook(@RequestBody @Valid InUserBookRequest req) {
        if (req.getUser() == null || req.getPlace() == null
                || req.getUser().getUser_id() == null
                || req.getPlace().getName() == null || req.getPlace().getName().isBlank()
                || req.getPlace().getDivision() == null || req.getPlace().getDivision().isBlank()) {
            throw new InvalidInputException("user_id, place.name, place.division이 필요합니다.");
        }

        var saved = userBookService.addByName(
                req.getUser().getUser_id(),
                req.getPlace().getName().trim(),
                req.getPlace().getDivision().trim()
        );

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "The place has been saved in the drawing.",
                "place_name", saved.placeName,
                "division", saved.division
        ));
    }
}
