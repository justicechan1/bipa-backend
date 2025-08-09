package com.bipa.backend.dto.user;

import lombok.Data;

@Data
public class CharacterRequest {
    private User user;

    @Data
    public static class User {
        private Long id;
    }
}
