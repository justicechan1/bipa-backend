package com.bipa.backend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FirstLoginResponse {
    private String status;
    private Long id;
}
