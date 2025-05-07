package com.example.kptc_smp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtTokenPairDto {
    String refreshToken;

    String accessToken;
}
