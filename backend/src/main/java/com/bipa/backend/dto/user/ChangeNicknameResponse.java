package com.bipa.backend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangeNicknameResponse {
    private String status;
    private String message;
}
