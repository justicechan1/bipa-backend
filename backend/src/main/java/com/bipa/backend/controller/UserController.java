package com.bipa.backend.controller;

import com.bipa.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bipa.backend.dto.FirstLoginRequest;
import com.bipa.backend.dto.FirstLoginResponse;

import com.bipa.backend.dto.ChangeNicknameResponse;
import com.bipa.backend.dto.ChangeNicknameRequest;

import com.bipa.backend.dto.CharacterResponse;
import com.bipa.backend.dto.CharacterRequest;
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/first_login")
    public ResponseEntity<FirstLoginResponse> firstLogin(@RequestBody FirstLoginRequest request) {
        Long userId = userService.createUserIfNotExists(request.getUser());
        return ResponseEntity.ok(new FirstLoginResponse("success", userId));
    }

    @PostMapping("/change_nickname")
    public ResponseEntity<ChangeNicknameResponse> changeNickname(@RequestBody ChangeNicknameRequest request) {
        Long userId = request.getUsers().getId();
        String newNickname = request.getUsers().getNickname();

        userService.changeNickname(userId, newNickname);

        return ResponseEntity.ok(new ChangeNicknameResponse("success", "name change complete"));
    }

    @PostMapping("/characters")
    public ResponseEntity<CharacterResponse> getCharacter(@RequestBody CharacterRequest request) {
        Long userId = request.getUser().getId();
        CharacterResponse.Characters characterDto = userService.getCharacterByUserId(userId);
        return ResponseEntity.ok(new CharacterResponse(characterDto));
    }
}
