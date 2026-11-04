package com.telar.TelarMVP.interfaces.service;

import com.telar.TelarMVP.dto.LoginRequest;
import com.telar.TelarMVP.dto.UsuarioCredenciales;

public interface AuthServiceInterface {
    UsuarioCredenciales validar(LoginRequest request);
}
