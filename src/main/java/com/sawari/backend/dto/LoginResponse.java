package com.sawari.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private Long id;
    private String message;
    private String name;
    private String email;
    private String role;
    private boolean verified;
}