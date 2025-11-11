package com.bipa.backend.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
public class KakaoAuthController {

    @Value("${app.kakao.authorize-uri}")
    private String authorizeUri;

    @Value("${app.kakao.client-id}")
    private String clientId;

    @Value("${app.kakao.redirect-uri}")
    private String redirectUri;

    // ✅ prompt=login을 프론트에서 받아 카카오로 그대로 전달
    @GetMapping("/auth/kakao/login")
    public ResponseEntity<Void> login(
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String prompt // ✅ 추가
    ) {
        String url = authorizeUri
                + "?response_type=code"
                + "&client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8)
                + "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8)
                + (state != null ? "&state=" + URLEncoder.encode(state, StandardCharsets.UTF_8) : "")
                + (prompt != null ? "&prompt=" + URLEncoder.encode(prompt, StandardCharsets.UTF_8) : ""); // ✅ 추가

        return ResponseEntity.status(302)
                .header("Location", url)
                .build();
    }
}
