package com.telar.TelarMVP.interfaces.service;

import com.telar.TelarMVP.entities.Usuario;

import java.util.List;

public interface UsuarioServiceInterface {
    List<Usuario> listar();
    Usuario buscarPorId(Integer id);
    Usuario crear(String nombre, String email, String password);
    Usuario actualizar(Integer id, String nombre, String email);
    void eliminar(Integer id);
}
