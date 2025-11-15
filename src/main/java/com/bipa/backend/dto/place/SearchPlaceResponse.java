package com.bipa.backend.dto.place;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class SearchPlaceResponse {

    private List<Result> results; // 명세서: Output.results[]에 place_name만  :contentReference[oaicite:2]{index=2}

    @Getter
    @AllArgsConstructor
    public static class Result {
        @JsonProperty("place_name")
        private String placeName;

        @JsonProperty("image_url")
        private String imageUrl;
    }
}
