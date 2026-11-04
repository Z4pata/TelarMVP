package com.telar.TelarMVP.services;

import com.telar.TelarMVP.entities.Cuenta;
import com.telar.TelarMVP.entities.TipoCuenta;
import com.telar.TelarMVP.exceptions.CuentaInvalidaException;
import com.telar.TelarMVP.exceptions.CuentaNoEncontradaException;
import com.telar.TelarMVP.exceptions.TipoCuentaNoEncontradaException;
import com.telar.TelarMVP.exceptions.UsuarioNoEncontradoException;
import com.telar.TelarMVP.repositories.CuentaRepository;
import com.telar.TelarMVP.interfaces.repository.UsuarioRepositoryInterface;
import com.telar.TelarMVP.interfaces.service.CuentaServiceInterface;
import com.telar.TelarMVP.repositories.TipoCuentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CuentaService implements CuentaServiceInterface {

    @Autowired
    private CuentaRepository cuentaRepository;
    @Autowired
    private UsuarioRepositoryInterface usuarioRepository;
    @Autowired
    private TipoCuentaRepository tipoCuentaRepository;



    @Override
    @Transactional(readOnly = true)
    public List<Cuenta> listar() {
        return cuentaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cuenta> listarPorUsuarioId(Integer usuarioId) {
        if (usuarioRepository.buscarPorId(usuarioId).isEmpty()) {
            throw new UsuarioNoEncontradoException(usuarioId);
        }
        return cuentaRepository.findAllByUsuarioId(usuarioId);
    }

    @Override
    @Transactional(readOnly = true)
    public Cuenta findById(Integer id) {
        return cuentaRepository.findById(id)
                .orElseThrow(() -> new CuentaNoEncontradaException(id));
    }

    @Override
    @Transactional
    public Cuenta insertar(Cuenta cuenta) {
        verificarRelaciones(cuenta);
        cuenta.setId(null);
        return cuentaRepository.save(cuenta);
    }

    @Override
    @Transactional
    public Cuenta actualizar(Integer id, Cuenta cuenta) {
        Cuenta existente = findById(id);
        verificarRelaciones(cuenta);
        existente.setUsuarioId(cuenta.getUsuarioId());
        existente.setTipoCuenta(cuenta.getTipoCuenta());
        existente.setEntidad(cuenta.getEntidad());
        existente.setDescripcion(cuenta.getDescripcion());
        return cuentaRepository.save(existente);
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        cuentaRepository.delete(findById(id));
    }

    private void verificarRelaciones(Cuenta cuenta) {
        if (cuenta == null) {
            throw new CuentaInvalidaException("Los datos de la cuenta son obligatorios");
        }
        if (cuenta.getUsuarioId() == null) {
            throw new CuentaInvalidaException("El usuario es obligatorio");
        }
        if (cuenta.getTipoCuenta() == null || cuenta.getTipoCuenta().getId() == null) {
            throw new CuentaInvalidaException("El tipo de cuenta es obligatorio");
        }

        if (usuarioRepository.buscarPorId(cuenta.getUsuarioId()).isEmpty()) {
            throw new UsuarioNoEncontradoException(cuenta.getUsuarioId());
        }

        TipoCuenta tipoCuenta = tipoCuentaRepository.findById(cuenta.getTipoCuenta().getId())
                .orElseThrow(() -> new TipoCuentaNoEncontradaException(cuenta.getTipoCuenta().getId()));
        cuenta.setTipoCuenta(tipoCuenta);
    }
}
