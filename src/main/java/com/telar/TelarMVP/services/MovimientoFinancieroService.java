package com.telar.TelarMVP.services;

import com.telar.TelarMVP.entities.MovimientoFinanciero;
import com.telar.TelarMVP.exceptions.CuentaNoEncontradaException;
import com.telar.TelarMVP.exceptions.MovimientoInvalidoException;
import com.telar.TelarMVP.exceptions.TipoMovimientoNoSoportadoException;
import com.telar.TelarMVP.repositories.MovimientoFinancieroRepository;
import com.telar.TelarMVP.interfaces.service.MovimientoFinancieroInterface;
import com.telar.TelarMVP.interfaces.service.ProcesadorMovimientoInterface;
import com.telar.TelarMVP.repositories.CategoriaRepository;
import com.telar.TelarMVP.repositories.CuentaRepository;
import com.telar.TelarMVP.repositories.TipoMovimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class MovimientoFinancieroService implements MovimientoFinancieroInterface {

    @Autowired
    private MovimientoFinancieroRepository movimientoRepository;
    @Autowired
    private CuentaRepository cuentaRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private TipoMovimientoRepository tipoMovimientoRepository;

    private final Map<String, ProcesadorMovimientoInterface> procesadores;

    public MovimientoFinancieroService(
            List<ProcesadorMovimientoInterface> estrategias
    ) {
        this.procesadores = estrategias.stream().collect(Collectors.toMap(
                ProcesadorMovimientoInterface::getCodigoTipo,
                Function.identity()
        ));
    }

    @Override
    @Transactional
    public MovimientoFinanciero crear(MovimientoFinanciero movimiento) {
        validarMovimiento(movimiento);
        movimiento.setId(null);
        movimiento.setCuenta(cuentaRepository.findById(movimiento.getCuenta().getId())
                .orElseThrow(() -> new CuentaNoEncontradaException(movimiento.getCuenta().getId())));
        movimiento.setCategoria(categoriaRepository.findById(movimiento.getCategoria().getId())
                .orElseThrow(() -> new EmptyResultDataAccessException(1)));
        movimiento.setTipoMovimiento(tipoMovimientoRepository.findById(movimiento.getTipoMovimiento().getId())
                .orElseThrow(() -> new EmptyResultDataAccessException(1)));

        String tipo = movimiento.getTipoMovimiento().getNombre();
        ProcesadorMovimientoInterface procesador = procesadores.get(tipo);

        if (procesador != null) {
            procesador.procesar(movimiento);
        }


        return movimientoRepository.save(movimiento);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoFinanciero> listarPorCuentaId(Integer cuentaId) {
        if (cuentaRepository.findById(cuentaId).isEmpty()) {
            throw new CuentaNoEncontradaException(cuentaId);
        }
        return movimientoRepository.findAllByCuenta_Id(cuentaId);
    }

    private void validarMovimiento(MovimientoFinanciero movimiento) {
        if (movimiento == null) {
            throw new MovimientoInvalidoException("Los datos del movimiento son obligatorios");
        }
        if (movimiento.getCuenta() == null || movimiento.getCuenta().getId() == null) {
            throw new MovimientoInvalidoException("La cuenta es obligatoria");
        }
        if (movimiento.getCategoria() == null || movimiento.getCategoria().getId() == null) {
            throw new MovimientoInvalidoException("La categoria es obligatoria");
        }
        if (movimiento.getTipoMovimiento() == null || movimiento.getTipoMovimiento().getId() == null) {
            throw new MovimientoInvalidoException("El tipo de movimiento es obligatorio");
        }
        if (movimiento.getMonto() == null || movimiento.getMonto().signum() <= 0) {
            throw new MovimientoInvalidoException("El monto debe ser mayor que cero");
        }
        if (movimiento.getFecha() == null) {
            throw new MovimientoInvalidoException("La fecha es obligatoria");
        }
        if (movimiento.getDescripcion() != null && movimiento.getDescripcion().length() > 255) {
            throw new MovimientoInvalidoException("La descripcion no puede superar los 255 caracteres");
        }
    }
}
