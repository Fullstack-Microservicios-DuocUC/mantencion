package cl.duoc.mineria.mantencion.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cl.duoc.mineria.mantencion.exception.MaintenanceNotFoundException;
import cl.duoc.mineria.mantencion.model.EstadoOrden;
import cl.duoc.mineria.mantencion.model.OrdenMantencion;
import cl.duoc.mineria.mantencion.repository.OrdenMantencionRepository;

@Service
public class OrdenMantencionService {

    private final OrdenMantencionRepository repository;
    private final ExternalUsuarioService usuarioService;
    private final ExternalEquipoService equipoService;

    // Inyección limpia resolviendo el antiguo servicio eliminado
    public OrdenMantencionService(OrdenMantencionRepository repository, 
                                  ExternalUsuarioService usuarioService, 
                                  ExternalEquipoService equipoService) {
        this.repository = repository;
        this.usuarioService = usuarioService;
        this.equipoService = equipoService;
    }

    @Transactional
    public OrdenMantencion crearOrden(OrdenMantencion orden) {
        // 1. Validar existencia del usuario en el sistema de personal (8081)
        boolean usuarioValido = usuarioService.verificarUsuarioExiste(orden.getReportadoPorUsuarioId());
        if (!usuarioValido) {
            throw new MaintenanceNotFoundException("No se puede registrar la orden de taller porque el ID de usuario " 
            + orden.getReportadoPorUsuarioId() + " no está registrado en el sistema de personal.");
        }

        // 2. Validar existencia del equipo en su tabla correspondiente usando el Enum discriminador
        boolean equipoValido = equipoService.verificarEquipoExiste(orden.getTipoEquipo(), orden.getEquipoId());
        if (!equipoValido) {
            throw new MaintenanceNotFoundException("No se puede registrar la pana porque el ID de equipo "
            + orden.getEquipoId() + " no existe en los inventarios de " + orden.getTipoEquipo() + ".");
        }

        return repository.saveAndFlush(orden);
    }

    @Transactional(readOnly = true)
    public List<OrdenMantencion> obtenerTodas() {
        return repository.findAll();
    }

    @Transactional
    public OrdenMantencion resolverOrden(Long id, EstadoOrden nuevoEstado) {
        OrdenMantencion orden = repository.findById(id)
        .orElseThrow(() -> new MaintenanceNotFoundException("La orden de mantención con ID " + id + " no existe en los registros del taller."));

        orden.setEstadoOrden(nuevoEstado);

        if (nuevoEstado == EstadoOrden.COMPLETADA) {
            orden.setFechaHoraResolucion(LocalDateTime.now());
        }

        return repository.saveAndFlush(orden);
    }

    @Transactional(readOnly = true)
    public List<OrdenMantencion> obtenerPorEquipo(Long equipoId) {
        return repository.findByEquipoId(equipoId);
    }

    @Transactional(readOnly = true)
    public List<OrdenMantencion> obtenerPorTurno(Long turnoId) {
        return repository.findByTurnoId(turnoId);
    }
}