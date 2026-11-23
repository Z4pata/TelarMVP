package com.telar.TelarMVP.services;

import com.telar.TelarMVP.dto.TipoMovimientoRequest;
import com.telar.TelarMVP.entities.TipoMovimiento;
import com.telar.TelarMVP.exceptions.ConflictoExcepcion;
import com.telar.TelarMVP.interfaces.service.TipoMovimientoServiceInterface;
import com.telar.TelarMVP.repositories.TipoMovimientoRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class TipoMovimientoService implements TipoMovimientoServiceInterface {

    // Los procesadores de movimientos dependen de estos nombres, por eso no se pueden tocar.
    private static final Set<String> TIPOS_PROTEGIDOS = Set.of(
            ProcesadorIngreso.CODIGO_TIPO,
            ProcesadorEgreso.CODIGO_TIPO
    );
    private static final String MENSAJE_PROTEGIDO =
            "Los tipos de movimiento Ingreso y Egreso no se pueden modificar ni eliminar";

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
        verificarNoProtegido(tipoMovimiento.getNombre());
        verificarNoProtegido(request.getNombre());
        tipoMovimiento.setNombre(request.getNombre());
        tipoMovimiento.setDescripcion(request.getDescripcion());
        return repository.save(tipoMovimiento);
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        TipoMovimiento tipoMovimiento = findById(id);
        verificarNoProtegido(tipoMovimiento.getNombre());
        repository.delete(tipoMovimiento);
    }

    private void verificarNoProtegido(String nombre) {
        boolean protegido = nombre != null && TIPOS_PROTEGIDOS.stream()
                .anyMatch(tipo -> tipo.equalsIgnoreCase(nombre.trim()));
        if (protegido) {
            throw new ConflictoExcepcion(MENSAJE_PROTEGIDO);
        }
    }
}
