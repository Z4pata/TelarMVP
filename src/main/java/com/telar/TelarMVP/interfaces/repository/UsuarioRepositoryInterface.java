package com.telar.TelarMVP.interfaces.repository;

import com.telar.TelarMVP.entities.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepositoryInterface {
    List<Usuario> listar();
    Optional<Usuario> buscarPorId(Integer id);
    Usuario crear(String nombre, String email, String passwordHash);
    boolean actualizar(Integer id, String nombre, String email);
    boolean eliminar(Integer id);
}
