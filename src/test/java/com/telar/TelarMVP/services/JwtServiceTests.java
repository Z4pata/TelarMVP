package com.telar.TelarMVP.services;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtServiceTests {

    private final JwtService jwtService = new JwtService();

    @Test
    void obtenerUsuarioIdLeeElClaimDelJwt() {
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("userId", 42)
                .build();

        assertEquals(42, jwtService.obtenerUsuarioId(jwt));
    }

    @Test
    void obtenerUsuarioIdFallaSiElClaimNoEsUnNumero() {
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("userId", "42")
                .build();

        assertThrows(IllegalArgumentException.class, () -> jwtService.obtenerUsuarioId(jwt));
    }
}
