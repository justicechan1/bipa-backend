package com.bipa.backend.dto.place;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlaceVisitDto {
    private String place_name;
    private boolean visit;
}