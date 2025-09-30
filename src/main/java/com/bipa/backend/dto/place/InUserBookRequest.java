package com.bipa.backend.dto.place;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class InUserBookRequest {
    private User user;
    private Place place;

    @Getter @Setter
    public static class User {
        private Long user_id;
    }

    @Getter @Setter
    public static class Place {
        private String name;      // place_name
        private String division;  // "cafe" | "restaurant"
    }
}
