package com.telar.TelarMVP.services;

import com.telar.TelarMVP.entities.Usuario;
import com.telar.TelarMVP.exceptions.UsuarioNoEncontradoException;
import com.telar.TelarMVP.repositories.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<Usuario> listar() {
        return usuarioRepository.listar();
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(Integer id) {
        return usuarioRepository.buscarPorId(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException(id));
    }

    @Transactional
    public Usuario crear(String nombre, String email, String password) {
        return usuarioRepository.crear(nombre, email, passwordEncoder.encode(password));
    }

    @Transactional
    public Usuario actualizar(Integer id, String nombre, String email) {
        if (!usuarioRepository.actualizar(id, nombre, email)) {
            throw new UsuarioNoEncontradoException(id);
        }
        return new Usuario(id, nombre, email);
    }

    @Transactional
    public void eliminar(Integer id) {
        if (!usuarioRepository.eliminar(id)) {
            throw new UsuarioNoEncontradoException(id);
        }
    }
}
