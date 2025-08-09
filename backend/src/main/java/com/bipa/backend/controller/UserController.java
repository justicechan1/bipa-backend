package com.bipa.backend.controller;

import com.bipa.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bipa.backend.exception.*;

import com.bipa.backend.dto.user.FirstLoginRequest;
import com.bipa.backend.dto.user.ChangeNicknameRequest;
import com.bipa.backend.dto.user.CharacterRequest;
import com.bipa.backend.dto.user.CharacterResponse;

import jakarta.validation.Valid;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    // /user/first_login
    @PostMapping("/first_login")
    public ResponseEntity<?> firstLogin(@RequestBody @Valid FirstLoginRequest req) {
        if (req == null || req.getUser() == null) {
            throw new InvalidInputException("user 입력이 필요합니다.");
        }
        Long newId = userService.firstLogin(req); // 내부에서 신규 user/character 생성
        return ResponseEntity.ok(Map.of("status", "success", "id", newId));
    }

    // /user/change_nickname
    @PostMapping("/change_nickname")
    public ResponseEntity<?> changeNickname(@RequestBody @Valid ChangeNicknameRequest req) {
        var u = req.getUsers();
        if (u == null || u.getId() == null || u.getNickname() == null || u.getNickname().isBlank()) {
            throw new InvalidInputException("users.id, users.nickname이 필요합니다.");
        }
        boolean ok = userService.changeNickname(u.getId(), u.getNickname());
        if (!ok) throw new NotFoundException("사용자를 찾을 수 없습니다.");

        // 명세 포맷: { "status": "success", "message": "name change complete" }
        return ResponseEntity.ok(Map.of("status", "success", "message", "name change complete"));
    }

    // /user/characters
    @PostMapping("/characters")
    public ResponseEntity<CharacterResponse> getCharacter(@RequestBody @Valid CharacterRequest request) {
        if (request == null || request.getUser() == null || request.getUser().getId() == null) {
            throw new InvalidInputException("user.id가 필요합니다.");
        }
        Long userId = request.getUser().getId();
        // 서비스 내부에서 유저/캐릭터가 없으면 NotFoundException 던지도록 구현
        CharacterResponse.Characters characterDto = userService.getCharacterByUserId(userId);
        return ResponseEntity.ok(new CharacterResponse(characterDto));
    }
}
