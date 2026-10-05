package com.telar.TelarMVP.interfaces.service;

import org.springframework.security.oauth2.jwt.Jwt;

public interface JwtServiceInterface {
    String crearToken(Integer userId);
}
