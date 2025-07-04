package com.example.kptc_smp.service.main.validator;

import com.example.kptc_smp.exception.jwt.JwtExpiredException;
import com.example.kptc_smp.utility.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtTokenValidatorService {
    private final JwtTokenUtils jwtTokenUtils;

    public void validateToken(String token) {
        if (isTokenExpired(token)) {
            throw new JwtExpiredException();
        }
    }

    public boolean isTokenExpired(String token) {
        Date expiration = jwtTokenUtils.getAllClaimsFromToken(token).getExpiration();
        return expiration.before(new Date());
    }

}