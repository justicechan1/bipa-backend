package com.bipa.backend.dto;

import lombok.Data;

@Data
public class TourPlaceDto {
    private String place_name;
    private String category;
    private String address;
    private String x_cord;
    private String y_cord;
    private String division;
}
