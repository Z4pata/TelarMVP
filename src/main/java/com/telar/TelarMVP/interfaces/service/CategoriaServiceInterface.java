package com.telar.TelarMVP.interfaces.service;

import com.telar.TelarMVP.dto.CategoriaRequest;
import com.telar.TelarMVP.entities.Categoria;

import java.util.List;

public interface CategoriaServiceInterface {

    List<Categoria> listar();

    Categoria findByNombre(String nombre);

    Categoria findById(Integer id);

    Categoria insertar(CategoriaRequest request);

    Categoria actualizar(Integer id, CategoriaRequest request);

    void eliminar(Integer id);
}
