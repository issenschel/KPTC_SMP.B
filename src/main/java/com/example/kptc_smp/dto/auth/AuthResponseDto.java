package com.example.kptc_smp.dto.auth;

import com.example.kptc_smp.dto.JwtTokenPairDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AuthResponseDto {
    private JwtTokenPairDto jwtTokenPairDto;
    private List<String> roles;
}
