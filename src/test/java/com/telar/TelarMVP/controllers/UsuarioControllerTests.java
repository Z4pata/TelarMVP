package com.telar.TelarMVP.controllers;

import com.telar.TelarMVP.entities.Usuario;
import com.telar.TelarMVP.services.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService usuarioService;

    @Test
    void listaUsuariosConGet() throws Exception {
        when(usuarioService.listar()).thenReturn(List.of(
                new Usuario(1, "Laura", "laura@example.com")
        ));

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Laura"));
    }

    @Test
    void creaUsuarioConPost() throws Exception {
        when(usuarioService.crear("Laura", "laura@example.com", "ClaveSegura123"))
                .thenReturn(new Usuario(1, "Laura", "laura@example.com"));

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombre": "Laura",
                                  "email": "laura@example.com",
                                  "password": "ClaveSegura123"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/usuarios/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void actualizaUsuarioConPut() throws Exception {
        when(usuarioService.actualizar(1, "Laura Gomez", "laura.gomez@example.com"))
                .thenReturn(new Usuario(1, "Laura Gomez", "laura.gomez@example.com"));

        mockMvc.perform(put("/api/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombre": "Laura Gomez",
                                  "email": "laura.gomez@example.com"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Laura Gomez"));
    }

    @Test
    void eliminaUsuarioConDelete() throws Exception {
        doNothing().when(usuarioService).eliminar(1);

        mockMvc.perform(delete("/api/usuarios/1"))
                .andExpect(status().isNoContent());
    }
}
