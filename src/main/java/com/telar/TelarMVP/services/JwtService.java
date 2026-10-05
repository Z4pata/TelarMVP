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

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey getKey() {
        return new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
    }



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
}
