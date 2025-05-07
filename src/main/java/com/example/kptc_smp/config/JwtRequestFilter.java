package com.example.kptc_smp.config;

import com.example.kptc_smp.service.main.auth.AuthUserDetailsService;
import com.example.kptc_smp.service.main.user.UserDataTokenService;
import com.example.kptc_smp.service.main.user.UserService;
import com.example.kptc_smp.utility.JwtTokenUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtTokenUtils jwtTokenUtils;
    private final UserDataTokenService userDataTokenService;
    private final AuthUserDetailsService authUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String username;
        String jwt;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            if (jwt.split("\\.").length == 3) {
                try {
                    username = jwtTokenUtils.getUsername(jwt);
                    Optional<UUID> tokenUUID = jwtTokenUtils.getTokenUUID(jwt);
                    if (username != null && tokenUUID.isPresent() && SecurityContextHolder.getContext().getAuthentication() == null &&
                        (userDataTokenService.findByTokenUUID(tokenUUID.get()).isPresent())) {
                        UserDetails userDetails = authUserDetailsService.loadUserByUsername(username);
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                } catch (ExpiredJwtException e) {
                    log.debug("Время жизни токена вышло");
                } catch (SignatureException e) {
                    log.debug("Подпись неправильная");
                }
            } else {
                log.debug("JWT не содержит трёх частей");
            }
        }

        filterChain.doFilter(request, response);

    }
}
