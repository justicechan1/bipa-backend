// src/main/java/com/bipa/backend/controller/KakaoCallbackController.java
package com.bipa.backend.controller;

import com.bipa.backend.auth.JwtTokenProvider;
import com.bipa.backend.auth.KakaoOAuthService;
import com.bipa.backend.entity.User;
import com.bipa.backend.repository.UserRepository;
import com.bipa.backend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class KakaoCallbackController {

    private final KakaoOAuthService kakaoOAuthService;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper om = new ObjectMapper();
    private final UserService userService;

    /**
     * 카카오 로그인 콜백
     * - 최초 로그인 시 사용자 생성
     * - 이후 로그인 시 닉네임 유지
     */
    @GetMapping(value = "/auth/kakao/callback", produces = "text/html; charset=UTF-8")
    public ResponseEntity<?> callback(
            @RequestParam String code,
            @RequestParam(value = "state", required = false) String state
    ) {
        try {
            // 1) code -> 카카오 토큰 요청
            Map<String, Object> tokenRes = kakaoOAuthService.exchangeCodeForToken(code);
            // access_token 로그는 보안상 제외
            log.info("[kakao tokenRes] token_type={}, expires_in={}, scope={}",
                    tokenRes.get("token_type"), tokenRes.get("expires_in"), tokenRes.get("scope"));

            String kakaoAccess = str(tokenRes.get("access_token"));
            if (kakaoAccess == null) {
                throw new IllegalStateException("Kakao access_token is null");
            }

            // 2) 사용자 정보 요청
            Map<String, Object> userInfo = kakaoOAuthService.getUserInfo(kakaoAccess);
            log.info("[kakao userInfo] keys={}", userInfo.keySet());

            // 3) 기본 정보 파싱
            Long kakaoId = asLong(userInfo.get("id"));
            if (kakaoId == null) throw new IllegalStateException("kakaoId is null");

            Map<String, Object> account = asMap(userInfo.get("kakao_account"));
            Map<String, Object> profile = asMap(account.get("profile"));

            String nickname = str(profile.get("nickname"));
            if (nickname == null || nickname.isBlank()) nickname = "카카오유저";

            String email = str(account.get("email"));
            String profileImage = str(profile.get("profile_image_url"));

            // 4) 사용자 upsert
            User user = userRepository.findByKakaoId(kakaoId).orElseGet(User::new);
            boolean isNew = (user.getId() == null);

            if (isNew) {
                // 신규 가입
                user.setKakaoId(kakaoId);
                user.setCreatedAt(LocalDateTime.now());
                user.setNickname(nickname); // ✅ 최초 한 번만 카카오 닉네임 설정
            } else {
                // 기존 유저: 닉네임 덮어쓰기 금지
                log.info("Existing user login: nickname preserved as '{}'", user.getNickname());
            }

            // 이메일, 프로필 이미지는 업데이트 가능
            if (email != null && !email.isBlank()) user.setEmail(email);
            if (profileImage != null && !profileImage.isBlank()) user.setProfileImage(profileImage);
            user.setConnectedAt(LocalDateTime.now());

            // 저장
            user = userRepository.save(user);
            userService.ensureDefaultCharacter(user.getId());

            // 5) JWT 토큰 발급
            String accessToken = jwtTokenProvider.createAccessToken(user.getId());
            String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());

            // 6) 프론트 postMessage 전달
            String targetOrigin = (state != null && state.startsWith("popup|"))
                    ? state.substring("popup|".length())
                    : "*";

            Map<String, Object> payload = new LinkedHashMap<>();
            payload.put("type", "KAKAO_LOGIN_DONE");
            payload.put("userId", user.getId());
            payload.put("kakaoId", user.getKakaoId());
            payload.put("nickname", user.getNickname());
            payload.put("email", user.getEmail());
            payload.put("profileImage", user.getProfileImage());
            payload.put("accessToken", accessToken);
            payload.put("refreshToken", refreshToken);

            String html = """
<!doctype html><html><head><meta charset="UTF-8"></head><body>
<script>
 (function() {
   const payload = %s;
   const targetOrigin = "%s";
   try {
     if (window.opener && !window.opener.closed) {
       console.log("[KAKAO CALLBACK] postMessage to", targetOrigin, payload);
       window.opener.postMessage(payload, targetOrigin);
       setTimeout(() => window.close(), 300);
     } else {
       document.body.innerText = "로그인 완료. 창을 닫아주세요.";
     }
   } catch (e) {
     document.body.innerText = "postMessage 실패: " + e;
   }
 })();
</script>
</body></html>
""".formatted(om.writeValueAsString(payload), targetOrigin);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "text/html; charset=UTF-8")
                    .body(html);

        } catch (Exception e) {
            log.error("[/auth/kakao/callback] failed", e);
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "error_code", "INTERNAL_ERROR",
                    "message", e.getClass().getSimpleName() + ": " +
                            (e.getMessage() == null ? "" : e.getMessage())
            ));
        }
    }

    // ---------- helpers ----------
    @SuppressWarnings("unchecked")
    private Map<String, Object> asMap(Object o) {
        return (o instanceof Map<?, ?> m) ? (Map<String, Object>) m : Map.of();
    }

    private String str(Object o) {
        return (o == null) ? null : o.toString();
    }

    private Long asLong(Object o) {
        if (o instanceof Number n) return n.longValue();
        try {
            return (o == null) ? null : Long.parseLong(o.toString());
        } catch (Exception ignore) {
            return null;
        }
    }
}
