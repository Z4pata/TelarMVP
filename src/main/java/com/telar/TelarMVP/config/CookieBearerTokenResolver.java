package com.telar.TelarMVP.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;

import java.util.Arrays;

/**
 * Obtiene el JWT desde la cookie de sesion.
 * Si no hay cookie, usa el header Authorization (util para Swagger y pruebas).
 */
public class CookieBearerTokenResolver implements BearerTokenResolver {

    private final BearerTokenResolver headerResolver = new DefaultBearerTokenResolver();

    @Override
    public String resolve(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return headerResolver.resolve(request);
        }

        return Arrays.stream(cookies)
                .filter(cookie -> SessionCookie.NAME.equals(cookie.getName()))
                .map(Cookie::getValue)
                .filter(value -> !value.isBlank())
                .findFirst()
                .orElseGet(() -> headerResolver.resolve(request));
    }
}
