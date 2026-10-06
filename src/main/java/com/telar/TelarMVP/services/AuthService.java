package com.telar.TelarMVP.services;

import com.telar.TelarMVP.dto.LoginRequest;
import com.telar.TelarMVP.dto.UsuarioCredenciales;
import com.telar.TelarMVP.exceptions.IncorrectPasswordException;
import com.telar.TelarMVP.interfaces.service.AuthServiceInterface;
import com.telar.TelarMVP.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService implements AuthServiceInterface {
    @Autowired
    UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional(readOnly = true)
    public UsuarioCredenciales validar(LoginRequest request){
        UsuarioCredenciales usuario = usuarioRepository.buscarPorEmail(request.getEmail());

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPasswordHash())){
            throw new IncorrectPasswordException("Contrasenia incorrecta.");
        }

        return usuario;
    }
}
