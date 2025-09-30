// controller/GameShopController.java
package com.bipa.backend.controller;

import com.bipa.backend.dto.game.ShopPlaceDto;
import com.bipa.backend.service.GameShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/game")
public class GameShopController {

    private final GameShopService service;

    public static class ShopRequest {
        public Place place;
        public static class Place { public String division; }
    }

    @PostMapping("/shop")
    public ResponseEntity<?> shop(@RequestBody ShopRequest req) {
        if (req == null || req.place == null || req.place.division == null || req.place.division.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status","error","error_code","INVALID_INPUT","message","place.division is required"));
        }
        List<ShopPlaceDto> list = service.listByDivision(req.place.division);
        return ResponseEntity.ok(Map.of("place", list));
    }
}
