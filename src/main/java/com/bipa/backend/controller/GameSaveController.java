// controller/GameSaveController.java
package com.bipa.backend.controller;

import com.bipa.backend.dto.game.SaveCharactersRequest;
import com.bipa.backend.service.GameSaveService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/game")
@RequiredArgsConstructor
public class GameSaveController {

    private final GameSaveService service;

    @PostMapping("/save")
    public ResponseEntity<?> save(@Valid @RequestBody SaveCharactersRequest req) {
        service.save(req.getCharacters());
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Game data saved"
        ));
    }
}
