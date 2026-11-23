package com.telar.TelarMVP.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * Construye la cookie HttpOnly que transporta el JWT de sesion.
 * El navegador la guarda y la reenvia; el JavaScript del frontend no puede leerla.
 */
@Component
public class SessionCookie {

    public static final String NAME = "telar_session";
    private static final Duration MAX_AGE = Duration.ofDays(1);

    private final boolean secure;

    public SessionCookie(@Value("${jwt.cookie.secure:false}") boolean secure) {
        this.secure = secure;
    }

    public ResponseCookie crear(String token) {
        return construir(token, MAX_AGE);
    }

    public ResponseCookie eliminar() {
        return construir("", Duration.ZERO);
    }

    private ResponseCookie construir(String value, Duration maxAge) {
        return ResponseCookie.from(NAME, value)
                .httpOnly(true)
                .secure(secure)
                .sameSite("Lax")
                .path("/")
                .maxAge(maxAge)
                .build();
    }
}
