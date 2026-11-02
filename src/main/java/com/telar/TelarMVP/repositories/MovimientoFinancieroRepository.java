package com.telar.TelarMVP.repositories;

import com.telar.TelarMVP.entities.MovimientoFinanciero;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimientoFinancieroRepository
        extends CrudRepository<MovimientoFinanciero, Integer> {

    List<MovimientoFinanciero> findAllByCuenta_Id(Integer cuentaId);

    @Query("""
            select coalesce(sum(movimiento.monto), 0)
            from MovimientoFinanciero movimiento
            where movimiento.cuenta.usuarioId = :usuarioId
              and movimiento.tipoMovimiento.nombre = :tipo
              and movimiento.fecha >= :inicio
              and movimiento.fecha < :fin
            """)
    BigDecimal sumarPorUsuarioTipoYPeriodo(
            @Param("usuarioId") Integer usuarioId,
            @Param("tipo") String tipo,
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );
}
