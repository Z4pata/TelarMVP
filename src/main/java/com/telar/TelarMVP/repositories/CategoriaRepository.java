package com.telar.TelarMVP.repositories;

import com.telar.TelarMVP.entities.Categoria;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends CrudRepository<Categoria, Integer> {

    @Override
    List<Categoria> findAll();

    Optional<Categoria> findByNombre(String nombre);
}
