package com.bipa.backend.dto.place;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SelectPlaceRequest {
    @NotNull
    private Place place;

    @Getter @Setter
    public static class Place {
        @NotBlank
        @JsonProperty("place_name")
        private String placeName;
    }
}
