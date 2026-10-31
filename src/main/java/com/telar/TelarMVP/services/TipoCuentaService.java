package com.telar.TelarMVP.services;

import com.telar.TelarMVP.dto.CrearTipoCuentaRequest;
import com.telar.TelarMVP.entities.TipoCuenta;
import com.telar.TelarMVP.interfaces.service.TipoCuentaServiceInterface;
import com.telar.TelarMVP.repositories.TipoCuentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TipoCuentaService implements TipoCuentaServiceInterface {

    @Autowired
    TipoCuentaRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<TipoCuenta> listar() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public TipoCuenta findByNombre(String nombre) {
        return repository.findByNombre(nombre)
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
    }

    @Override
    @Transactional(readOnly = true)
    public TipoCuenta findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
    }

    @Override
    @Transactional
    public TipoCuenta insertar(CrearTipoCuentaRequest request) {
        TipoCuenta tipoCuenta = new TipoCuenta(null, request.getNombre(), request.getDescripcion());
        return repository.save(tipoCuenta);
    }
}
