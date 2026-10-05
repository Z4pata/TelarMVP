package com.telar.TelarMVP.repositories;

import com.telar.TelarMVP.dto.PresupuestoResponse;
import com.telar.TelarMVP.entities.Presupuesto;
import com.telar.TelarMVP.entities.Usuario;
import com.telar.TelarMVP.exceptions.PresupuestoNoEncontradoException;
import com.telar.TelarMVP.interfaces.repository.PresupuestoRepositoryInterface;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class PresupuestoRepository implements PresupuestoRepositoryInterface {
    private static final String COLUMNAS = "id, usuario_id, monto_limite, fecha_inicio, fecha_fin";

    private final JdbcTemplate jdbcTemplate;
    public PresupuestoRepository(JdbcTemplate jdbcTemplate) {this.jdbcTemplate = jdbcTemplate;}

    public Presupuesto crear(Presupuesto presupuesto) {
        String sql = "INSERT INTO presupuesto (monto_limite, fecha_inicio, fecha_fin, usuario_id) VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setDouble(1, presupuesto.getMontoLimite());
            statement.setTimestamp(2, Timestamp.valueOf(presupuesto.getFechaInicio()));
            statement.setTimestamp(3, Timestamp.valueOf(presupuesto.getFechaFin()));
            statement.setString(4, presupuesto.getUsuario().id().toString());
            return statement;
        }, keyHolder);

        Number idGenerado = keyHolder.getKey();
        if (idGenerado == null) {
            throw new IllegalStateException("La base de datos no devolvio el id del presupuesto creado");
        }
        presupuesto.setId(idGenerado.intValue());

        return presupuesto;
    }

    public PresupuestoResponse buscarActualPorUsuario(Integer userId){
        String sql = "SELECT * FROM presupuesto WHERE usuario_id = ? AND fecha_inicio <= ? AND fecha_fin >= ?";
        LocalDateTime hoy = LocalDateTime.now();

        try {
            return jdbcTemplate.queryForObject(sql, this::mapearPresupuesto, userId, hoy, hoy);
        } catch (EmptyResultDataAccessException e) {
            throw new PresupuestoNoEncontradoException(
                    "No se encontro un presupuesto actual para el usuario " + userId);
        }
    }

    public Boolean existeActivo(Integer userId){
        String sql = "SELECT EXISTS(\n" +
                "    SELECT 1\n" +
                "    FROM presupuesto\n" +
                "    WHERE usuario_id = ?\n" +
                "    AND fecha_inicio <= ?\n" +
                "    AND fecha_fin >= ?\n" +
                ")";

        LocalDateTime ahora = LocalDateTime.now();

        return jdbcTemplate.queryForObject(sql, Boolean.class, userId, ahora, ahora);
    }

    private PresupuestoResponse mapearPresupuesto(java.sql.ResultSet resultSet, int rowNumber) throws java.sql.SQLException {

        return new PresupuestoResponse(
                resultSet.getInt("id"),
                resultSet.getInt("usuario_id"),
                resultSet.getDouble("monto_limite"),
                resultSet.getTimestamp("fecha_inicio").toLocalDateTime(),
                resultSet.getTimestamp("fecha_fin").toLocalDateTime()
        );
    }

}
