package com.bipa.backend.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class KakaoOAuthService {

    @Value("${app.kakao.token-uri}")
    private String tokenUri;

    @Value("${app.kakao.userinfo-uri}")
    private String userinfoUri;

    @Value("${app.kakao.client-id}")
    private String clientId;

    @Value("${app.kakao.redirect-uri}")
    private String redirectUri;

    // client-secret을 쓰지 않으면 굳이 안 넣어도 됨
    @Value("${app.kakao.client-secret:}")
    private String clientSecret;

    private final RestTemplate rest = new RestTemplate();

    // code -> access_token 교환
    public Map<String, Object> exchangeCodeForToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);
        if (clientSecret != null && !clientSecret.isBlank()) {
            body.add("client_secret", clientSecret);
        }

        ResponseEntity<Map> res = rest.exchange(
                tokenUri, HttpMethod.POST, new HttpEntity<>(body, headers), Map.class);

        return res.getBody(); // access_token, refresh_token 등 포함
    }

    // access_token -> 사용자 정보
    public Map<String, Object> getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        ResponseEntity<Map> res = rest.exchange(
                userinfoUri, HttpMethod.POST, new HttpEntity<>(new LinkedMultiValueMap<>(), headers), Map.class);

        return res.getBody();
    }
}
