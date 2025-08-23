package com.bipa.backend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CharacterResponse {
    private Characters characters;

    @Data
    @AllArgsConstructor
    public static class Characters {
        private Long id;
        private Long user_id;
        private int level;
        private int exp;
        private int money;
        private int hungry_gauge;
        private int heart_gauge;
        private int max_actopus;
        private int max_fig;
        private int max_yudal;
        private int max_fish;
    }
}
