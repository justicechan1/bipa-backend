package com.bipa.backend.dto.user;

import lombok.Data;

@Data
public class ChangeNicknameRequest {
    private Users users;

    @Data
    public static class Users {
        private Long id;         // 사용자 구분을 위해 필요
        private String nickname; // 새 닉네임
    }
}
