package com.telar.TelarMVP.repositories;

import com.telar.TelarMVP.entities.TipoCuenta;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TipoCuentaRepository extends CrudRepository<TipoCuenta, Integer> {

    @Override
    @NullMarked
    List<TipoCuenta> findAll();

    Optional<TipoCuenta> findByNombre(String nombre);

    @Override
    @NullMarked
    Optional<TipoCuenta> findById(Integer id);

}
