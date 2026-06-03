package cl.duoc.mineria.mantencion.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.duoc.mineria.mantencion.model.EstadoOrden;
import cl.duoc.mineria.mantencion.model.OrdenMantencion;
import cl.duoc.mineria.mantencion.model.Prioridad;

public interface OrdenMantencionRepository extends JpaRepository<OrdenMantencion, Long> {

    List<OrdenMantencion> findByEstadoOrden(EstadoOrden estadoOrden);

    List<OrdenMantencion> findByPrioridad(Prioridad prioridad);

    List<OrdenMantencion> findByEquipoId(Long equipoId);

    List<OrdenMantencion> findByTurnoId(Long turnoId);
}
