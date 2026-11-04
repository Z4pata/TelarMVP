package com.telar.TelarMVP.services;

import com.telar.TelarMVP.dto.CategoriaRequest;
import com.telar.TelarMVP.entities.Categoria;
import com.telar.TelarMVP.interfaces.service.CategoriaServiceInterface;
import com.telar.TelarMVP.repositories.CategoriaRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaService implements CategoriaServiceInterface {

    private final CategoriaRepository repository;

    public CategoriaService(CategoriaRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Categoria> listar() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Categoria findByNombre(String nombre) {
        return repository.findByNombre(nombre)
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
    }

    @Override
    @Transactional(readOnly = true)
    public Categoria findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
    }

    @Override
    @Transactional
    public Categoria insertar(CategoriaRequest request) {
        Categoria categoria = new Categoria(null, request.getNombre(), request.getDescripcion());
        return repository.save(categoria);
    }

    @Override
    @Transactional
    public Categoria actualizar(Integer id, CategoriaRequest request) {
        Categoria categoria = findById(id);
        categoria.setNombre(request.getNombre());
        categoria.setDescripcion(request.getDescripcion());
        return repository.save(categoria);
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        Categoria categoria = findById(id);
        repository.delete(categoria);
    }
}
