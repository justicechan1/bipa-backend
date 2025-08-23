package com.bipa.backend.dto.game;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ShopPlaceDto {
    @JsonProperty("place_name")
    private String placeName;

    @JsonProperty("address")
    private String address;

    @JsonProperty("hungry_gauge")
    private Integer hungryGauge;

    @JsonProperty("first_price")
    private Integer firstPrice;

    @JsonProperty("first_menu")
    private String firstMenu;
}