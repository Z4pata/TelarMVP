package com.telar.TelarMVP.services;

import com.telar.TelarMVP.dto.TipoMovimientoRequest;
import com.telar.TelarMVP.entities.TipoMovimiento;
import com.telar.TelarMVP.interfaces.service.TipoMovimientoServiceInterface;
import com.telar.TelarMVP.repositories.TipoMovimientoRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TipoMovimientoService implements TipoMovimientoServiceInterface {

    private final TipoMovimientoRepository repository;

    public TipoMovimientoService(TipoMovimientoRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoMovimiento> listar() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public TipoMovimiento findByNombre(String nombre) {
        return repository.findByNombre(nombre)
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
    }

    @Override
    @Transactional(readOnly = true)
    public TipoMovimiento findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
    }

    @Override
    @Transactional
    public TipoMovimiento insertar(TipoMovimientoRequest request) {
        TipoMovimiento tipoMovimiento = new TipoMovimiento(
                null,
                request.getNombre(),
                request.getDescripcion()
        );
        return repository.save(tipoMovimiento);
    }

    @Override
    @Transactional
    public TipoMovimiento actualizar(Integer id, TipoMovimientoRequest request) {
        TipoMovimiento tipoMovimiento = findById(id);
        tipoMovimiento.setNombre(request.getNombre());
        tipoMovimiento.setDescripcion(request.getDescripcion());
        return repository.save(tipoMovimiento);
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        TipoMovimiento tipoMovimiento = findById(id);
        repository.delete(tipoMovimiento);
    }
}
