package com.bipa.backend.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private String status;     // "error"
    private String error_code; // "INVALID_INPUT","PLACE_NOT_FOUND" 등
    private String message;    // 설명
}