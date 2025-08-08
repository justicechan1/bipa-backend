package com.bipa.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;
@Data
public class FirstLoginRequest {
    private UserDto user;

    @Data
    public static class UserDto {
        private Long id;
        private String nickname;
        private LocalDateTime connected_at;
    }
}

