package cl.duoc.mineria.mantencion.mapper;

import java.time.LocalDateTime;
import org.springframework.stereotype.Component;
import cl.duoc.mineria.mantencion.dto.ReportarPanaDTO;
import cl.duoc.mineria.mantencion.model.EstadoOrden;
import cl.duoc.mineria.mantencion.model.OrdenMantencion;

@Component
public class MantencionMapper {
    
    public OrdenMantencion toEntity(ReportarPanaDTO dto) {
        if (dto == null) return null;

        OrdenMantencion orden = new OrdenMantencion();
        orden.setTurnoId(dto.getTurnoId());
        orden.setTipoEquipo(dto.getTipoEquipo());
        orden.setEquipoId(dto.getEquipoId() != null ? dto.getEquipoId() : 0L);
        orden.setReportadoPorUsuarioId(dto.getReportadoPorUsuarioId() != null ? dto.getReportadoPorUsuarioId() : 0L);
        orden.setDescripcionFalla(dto.getDescripcionFalla());
        orden.setPrioridad(dto.getPrioridad());
        orden.setEstadoOrden(EstadoOrden.PENDIENTE);
        orden.setFechaHoraReporte(LocalDateTime.now());

        return orden;
    }
}