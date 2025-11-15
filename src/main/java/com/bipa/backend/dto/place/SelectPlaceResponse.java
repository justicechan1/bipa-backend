package com.bipa.backend.dto.place;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class SelectPlaceResponse {
    private Place place;

    @Getter
    @AllArgsConstructor
    public static class Place {
        @JsonProperty("place_name")
        private String placeName;
        private String category;
        private String address;
        @JsonProperty("business_hours")
        private String businessHours;
        private List<String> menu;
        @JsonProperty("image_url")
        private List<String> imageUrl;
    }
}
