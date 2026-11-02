package com.telar.TelarMVP.repositories;

import com.telar.TelarMVP.entities.Cuenta;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CuentaRepository extends CrudRepository<Cuenta, Integer> {

    @Override
    @NullMarked
    List<Cuenta> findAll();

    @NullMarked
    List<Cuenta> findAllByUsuarioId(Integer usuarioId);
}
