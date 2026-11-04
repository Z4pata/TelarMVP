package com.telar.TelarMVP.services;

import com.telar.TelarMVP.dto.CategoriaRequest;
import com.telar.TelarMVP.entities.Categoria;
import com.telar.TelarMVP.repositories.CategoriaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTests {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    @Test
    void listarCategorias() {
        List<Categoria> lista = List.of(
                new Categoria(1, "Alimentación", "Gastos de comida"),
                new Categoria(2, "Transporte", "Gastos de movilidad")
        );
        when(categoriaRepository.findAll()).thenReturn(lista);

        List<Categoria> result = categoriaService.listar();

        assertNotNull(result);
        assertEquals(lista.size(), result.size());
        assertEquals(lista, result);
        verify(categoriaRepository).findAll();
    }

    @Test
    void listarCategoriasVacio() {
        List<Categoria> lista = List.of();
        when(categoriaRepository.findAll()).thenReturn(lista);

        List<Categoria> result = categoriaService.listar();

        assertEquals(lista, result);
        verify(categoriaRepository).findAll();
    }

    @Test
    void encontrarCategoriaPorNombre() {
        String nombre = "Alimentación";
        Categoria categoria = new Categoria(1, nombre, "Gastos de comida");
        when(categoriaRepository.findByNombre(nombre)).thenReturn(Optional.of(categoria));

        Categoria result = categoriaService.findByNombre(nombre);

        assertNotNull(result);
        assertEquals(categoria, result);
        verify(categoriaRepository).findByNombre(nombre);
    }

    @Test
    void encontrarCategoriaPorNombre_retornaNoEncontrado() {
        String nombre = "Alimentación";
        when(categoriaRepository.findByNombre(nombre)).thenReturn(Optional.empty());

        assertThrows(EmptyResultDataAccessException.class, () -> categoriaService.findByNombre(nombre));
        verify(categoriaRepository).findByNombre(nombre);
    }

    @Test
    void encontrarCategoriaPorId() {
        Integer id = 1;
        Categoria categoria = new Categoria(id, "Alimentación", "Gastos de comida");
        when(categoriaRepository.findById(id)).thenReturn(Optional.of(categoria));

        Categoria result = categoriaService.findById(id);

        assertNotNull(result);
        assertEquals(categoria, result);
        verify(categoriaRepository).findById(id);
    }

    @Test
    void encontrarCategoriaPorId_retornaNoEncontrado() {
        Integer id = 99;
        when(categoriaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EmptyResultDataAccessException.class, () -> categoriaService.findById(id));
        verify(categoriaRepository).findById(id);
    }

    @Test
    void insertarCategoria() {
        CategoriaRequest request = new CategoriaRequest();
        request.setNombre("Alimentación");
        request.setDescripcion("Gastos de comida");
        Categoria guardada = new Categoria(1, "Alimentación", "Gastos de comida");
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(guardada);

        Categoria result = categoriaService.insertar(request);

        ArgumentCaptor<Categoria> captor = ArgumentCaptor.forClass(Categoria.class);
        verify(categoriaRepository).save(captor.capture());
        assertNull(captor.getValue().getId());
        assertEquals("Alimentación", captor.getValue().getNombre());
        assertEquals("Gastos de comida", captor.getValue().getDescripcion());
        assertEquals(guardada, result);
    }

    @Test
    void insertarCategoria_retornaErrorCuandoViolaRestriccion() {
        CategoriaRequest request = new CategoriaRequest();
        request.setNombre("Alimentación");
        request.setDescripcion("Gastos de comida");
        when(categoriaRepository.save(any(Categoria.class)))
                .thenThrow(new DataIntegrityViolationException("Restriccion de base de datos"));

        assertThrows(DataIntegrityViolationException.class, () -> categoriaService.insertar(request));
        verify(categoriaRepository).save(any(Categoria.class));
    }

    @Test
    void actualizarCategoria() {
        Integer id = 1;
        Categoria existente = new Categoria(id, "Alimentación", "Descripcion anterior");
        CategoriaRequest request = new CategoriaRequest();
        request.setNombre("Comida");
        request.setDescripcion("Descripcion actualizada");
        when(categoriaRepository.findById(id)).thenReturn(Optional.of(existente));
        when(categoriaRepository.save(existente)).thenReturn(existente);

        Categoria result = categoriaService.actualizar(id, request);

        assertEquals(id, result.getId());
        assertEquals("Comida", result.getNombre());
        assertEquals("Descripcion actualizada", result.getDescripcion());
        verify(categoriaRepository).findById(id);
        verify(categoriaRepository).save(existente);
    }

    @Test
    void actualizarCategoria_retornaErrorCuandoNoExiste() {
        Integer id = 99;
        CategoriaRequest request = new CategoriaRequest();
        request.setNombre("Comida");
        request.setDescripcion("Descripcion");
        when(categoriaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EmptyResultDataAccessException.class, () -> categoriaService.actualizar(id, request));
        verify(categoriaRepository).findById(id);
        verify(categoriaRepository, never()).save(any(Categoria.class));
    }

    @Test
    void actualizarCategoria_retornaErrorCuandoViolaRestriccion() {
        Integer id = 1;
        Categoria existente = new Categoria(id, "Alimentación", "Descripcion anterior");
        CategoriaRequest request = new CategoriaRequest();
        request.setNombre("Comida");
        request.setDescripcion("Descripcion actualizada");
        when(categoriaRepository.findById(id)).thenReturn(Optional.of(existente));
        when(categoriaRepository.save(existente))
                .thenThrow(new DataIntegrityViolationException("Restriccion de base de datos"));

        assertThrows(DataIntegrityViolationException.class, () -> categoriaService.actualizar(id, request));
        verify(categoriaRepository).save(existente);
    }

    @Test
    void eliminarCategoria() {
        Integer id = 1;
        Categoria existente = new Categoria(id, "Alimentación", "Gastos de comida");
        when(categoriaRepository.findById(id)).thenReturn(Optional.of(existente));

        categoriaService.eliminar(id);

        verify(categoriaRepository).findById(id);
        verify(categoriaRepository).delete(existente);
    }

    @Test
    void eliminarCategoria_retornaErrorCuandoNoExiste() {
        Integer id = 99;
        when(categoriaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EmptyResultDataAccessException.class, () -> categoriaService.eliminar(id));
        verify(categoriaRepository).findById(id);
        verify(categoriaRepository, never()).delete(any(Categoria.class));
    }
}
