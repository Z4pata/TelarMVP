package com.telar.TelarMVP.controllers;

import com.telar.TelarMVP.dto.CrearTipoCuentaRequest;
import com.telar.TelarMVP.entities.TipoCuenta;
import com.telar.TelarMVP.interfaces.Api.TipoCuentaApiInterface;
import com.telar.TelarMVP.interfaces.service.TipoCuentaServiceInterface;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@Tag(name = "TipoCuenta",
        description = "Endpoints para el manejo de tipos de cuenta")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/tipos-cuenta")
public class TipoCuentaController implements TipoCuentaApiInterface {
    @Autowired
    private TipoCuentaServiceInterface tipoCuentaService;

    @Override
    public ResponseEntity<TipoCuenta> crear(@Valid @RequestBody CrearTipoCuentaRequest tipoCuenta) {
        TipoCuenta tipo = tipoCuentaService.insertar(tipoCuenta);
        return ResponseEntity.created(URI.create("/api/tipos-cuenta/" + tipo.getId())).body(tipo);
    }

    @Override
    public ResponseEntity<List<TipoCuenta>> listar() {
        return ResponseEntity.ok().body(tipoCuentaService.listar());
    }

    @Override
    public ResponseEntity<TipoCuenta> findByNombre(@PathVariable("nombre") String nombre) {
        return ResponseEntity.ok().body(tipoCuentaService.findByNombre(nombre)) ;
    }

    @Override
    public ResponseEntity<TipoCuenta> findById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok().body(tipoCuentaService.findById(id));
    }


}
