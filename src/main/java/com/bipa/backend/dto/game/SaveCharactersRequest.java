// dto/game/SaveCharactersRequest.java
package com.bipa.backend.dto.game;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SaveCharactersRequest {

    @JsonProperty("characters")
    @NotNull
    private CharactersPayload characters;

    @Getter @Setter
    public static class CharactersPayload {
        @JsonProperty("user_id") @NotNull
        private Long userId;               // 저장 기준 키

        private Integer level;
        private Integer exp;
        private Integer money;

        @JsonProperty("hungry_gauge")
        private Integer hungryGauge;       // 0~100

        @JsonProperty("heart_gauge")
        private Integer heartGauge;        // 0~100

        @JsonProperty("max_actopus")
        private Integer maxActopus;

        @JsonProperty("max_fig")
        private Integer maxFig;

        @JsonProperty("max_yudal")
        private Integer maxYudal;

        @JsonProperty("max_fish")
        private Integer maxFish;
    }
}
