package com.telar.TelarMVP.repositories;

import com.telar.TelarMVP.entities.TipoMovimiento;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TipoMovimientoRepository extends CrudRepository<TipoMovimiento, Integer> {

    @Override
    List<TipoMovimiento> findAll();

    Optional<TipoMovimiento> findByNombre(String nombre);
}
