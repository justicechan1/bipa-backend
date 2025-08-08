package com.bipa.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangeNicknameResponse {
    private String status;
    private String message;
}
