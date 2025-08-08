package com.bipa.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FirstLoginResponse {
    private String status;
    private Long id;
}
