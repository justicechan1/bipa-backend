package com.bipa.backend.dto.place;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SearchPlaceRequest {
    @NotBlank
    private String search;   // 명세서: Input { "search": "string" }  :contentReference[oaicite:1]{index=1}
}
