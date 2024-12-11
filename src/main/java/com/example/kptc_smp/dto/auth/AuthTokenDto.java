package com.example.kptc_smp.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AuthTokenDto {
    private String token;
    private List<String> role;
}
