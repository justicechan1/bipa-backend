package com.bipa.backend.controller;

import org.springframework.security.core.Authentication;   // ✅ 추가
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class MeController {

    @GetMapping("/api/me")
    public Map<String, Object> me(Authentication auth) {
        // JwtAuthFilter가 principal.username에 userId를 넣었음
        return Map.of("userId", auth.getName());
    }
}
