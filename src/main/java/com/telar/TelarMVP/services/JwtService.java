package com.telar.TelarMVP.services;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.telar.TelarMVP.interfaces.service.JwtServiceInterface;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class JwtService implements JwtServiceInterface {
    private static final String USER_ID_CLAIM = "userId";

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey getKey() {
        return new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
    }

    @Override
    public String crearToken(Integer userId) {

        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(userId.toString())
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.DAYS))
                .claim("userId", userId)
                .build();

        JwsHeader header = JwsHeader.with(() -> "HS256").build();

        JwtEncoder encoder = new NimbusJwtEncoder(
                new ImmutableSecret<>(getKey())
        );

        return encoder.encode(
                JwtEncoderParameters.from(header, claims)
        ).getTokenValue();
    }

    @Override
    public Integer obtenerUsuarioId(Jwt jwt) {
        Object claim = jwt.getClaims().get(USER_ID_CLAIM);
        if (!(claim instanceof Number number)) {
            throw new IllegalArgumentException("El JWT no contiene un claim userId numerico.");
        }

        long userId = number.longValue();
        if (number.doubleValue() != userId || userId < Integer.MIN_VALUE || userId > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("El claim userId del JWT debe ser un entero de 32 bits.");
        }

        return (int) userId;
    }
}
