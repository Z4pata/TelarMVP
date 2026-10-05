package com.telar.TelarMVP.services;

import com.telar.TelarMVP.dto.CrearPresupuestoRequest;
import com.telar.TelarMVP.dto.PresupuestoResponse;
import com.telar.TelarMVP.entities.Presupuesto;
import com.telar.TelarMVP.entities.Usuario;
import com.telar.TelarMVP.exceptions.ConflictoExcepcion;
import com.telar.TelarMVP.exceptions.UsuarioNoEncontradoException;
import com.telar.TelarMVP.interfaces.repository.PresupuestoRepositoryInterface;
import com.telar.TelarMVP.interfaces.repository.UsuarioRepositoryInterface;
import com.telar.TelarMVP.interfaces.service.PresupuestoServiceInterface;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PresupuestoService implements PresupuestoServiceInterface {
    private final PresupuestoRepositoryInterface presupuestoRepository;
    private final UsuarioRepositoryInterface usuarioRepository;

    public PresupuestoService(PresupuestoRepositoryInterface presupuestoRepository, UsuarioRepositoryInterface usuarioRepository) {
        this.presupuestoRepository = presupuestoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Presupuesto crear(CrearPresupuestoRequest request){
        Optional<Usuario> usuario = usuarioRepository.buscarPorId(request.getUsuarioId());

        if(usuario.isEmpty()){
            throw new UsuarioNoEncontradoException(request.getUsuarioId());
        }
        if(presupuestoRepository.existeActivo(request.getUsuarioId())){
            throw new ConflictoExcepcion("El usuario ya tiene un presupuesto activo en ese intervalo de tiempo");
        }

        Presupuesto presupuesto = new Presupuesto();
        presupuesto.setUsuario(usuario.get());
        presupuesto.setMontoLimite(request.getMontoLimite());
        presupuesto.setFechaInicio(LocalDateTime.now());
        presupuesto.setFechaFin(request.getFechaFin());

        return presupuestoRepository.crear(presupuesto);
    }

    @Transactional
    public PresupuestoResponse buscarActualPorUsuario(Integer usuarioId){
        return presupuestoRepository.buscarActualPorUsuario(usuarioId);

    }


}
