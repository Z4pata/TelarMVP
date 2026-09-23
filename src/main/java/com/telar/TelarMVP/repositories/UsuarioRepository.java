package com.telar.TelarMVP.repositories;

import com.telar.TelarMVP.entities.Usuario;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class UsuarioRepository {

    private static final String COLUMNAS = "id, nombre, email";

    private final JdbcTemplate jdbcTemplate;

    public UsuarioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Usuario> listar() {
        String sql = "SELECT " + COLUMNAS + " FROM usuario ORDER BY id";
        return jdbcTemplate.query(sql, this::mapearUsuario);
    }

    public Optional<Usuario> buscarPorId(Integer id) {
        String sql = "SELECT " + COLUMNAS + " FROM usuario WHERE id = ?";
        return jdbcTemplate.query(sql, this::mapearUsuario, id).stream().findFirst();
    }

    public Usuario crear(String nombre, String email, String passwordHash) {
        String sql = "INSERT INTO usuario (nombre, email, password_hash) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, nombre);
            statement.setString(2, email);
            statement.setString(3, passwordHash);
            return statement;
        }, keyHolder);

        Number idGenerado = keyHolder.getKey();
        if (idGenerado == null) {
            throw new IllegalStateException("La base de datos no devolvio el id del usuario creado");
        }

        return new Usuario(idGenerado.intValue(), nombre, email);
    }

    public boolean actualizar(Integer id, String nombre, String email) {
        String sql = "UPDATE usuario SET nombre = ?, email = ? WHERE id = ?";
        return jdbcTemplate.update(sql, nombre, email, id) == 1;
    }

    public boolean eliminar(Integer id) {
        String sql = "DELETE FROM usuario WHERE id = ?";
        return jdbcTemplate.update(sql, id) == 1;
    }

    private Usuario mapearUsuario(java.sql.ResultSet resultSet, int rowNumber) throws java.sql.SQLException {
        return new Usuario(
                resultSet.getInt("id"),
                resultSet.getString("nombre"),
                resultSet.getString("email")
        );
    }
}
