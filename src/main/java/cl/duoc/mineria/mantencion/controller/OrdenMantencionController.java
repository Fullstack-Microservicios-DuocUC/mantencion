package cl.duoc.mineria.mantencion.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import cl.duoc.mineria.mantencion.dto.ReportarPanaDTO;
import cl.duoc.mineria.mantencion.dto.ResolverOrdenDTO;
import cl.duoc.mineria.mantencion.mapper.MantencionMapper;
import cl.duoc.mineria.mantencion.model.OrdenMantencion;
import cl.duoc.mineria.mantencion.service.OrdenMantencionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/mantenciones")
@Tag(name = "Gestión de Mantenciones", description = "Operaciones para crear y gestionar órdenes de mantención por fallas en equipos.")
public class OrdenMantencionController {

    private final OrdenMantencionService service;
    private final MantencionMapper mapper;

    public OrdenMantencionController(OrdenMantencionService service, MantencionMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping("/reportar")
    @Operation(summary = "Reportar una nueva falla", description = "Crea una nueva orden de mantención a partir de una falla reportada por un operario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Orden de mantención creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<OrdenMantencion> reportarPana(@Valid @RequestBody ReportarPanaDTO dto) {
        OrdenMantencion nuevaOrden = mapper.toEntity(dto);
        OrdenMantencion guardada = service.crearOrden(nuevaOrden);
        return new ResponseEntity<>(guardada, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Listar todas las órdenes de mantención", description = "Obtiene una lista completa de todas las órdenes de mantención registradas.")
    @ApiResponse(responseCode = "200", description = "Lista de órdenes obtenida con éxito")
    public ResponseEntity<List<OrdenMantencion>> listarTodas() {
        return ResponseEntity.ok(service.obtenerTodas());
    }

    @PutMapping("/resolver")
    @Operation(summary = "Resolver o actualizar una orden de mantención", description = "Actualiza el estado de una orden de mantención existente (ej. a 'RESUELTA' o 'EN_PROGRESO').")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orden actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada con el ID proporcionado")
    })
    public ResponseEntity<OrdenMantencion> resolverOrden(@Valid @RequestBody ResolverOrdenDTO dto) {
        OrdenMantencion actualizada = service.resolverOrden(dto.getId(), dto.getEstadoOrden());
        return ResponseEntity.ok(actualizada);
    }

    @GetMapping("/equipo/{equipoId}")
    @Operation(summary = "Listar órdenes por equipo", description = "Obtiene el historial de mantenciones para un equipo específico (camión o pala) a través de su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historial de mantenciones del equipo obtenido con éxito")
    })
    public ResponseEntity<List<OrdenMantencion>> listarPorEquipo(@PathVariable Long equipoId) {
        return ResponseEntity.ok(service.obtenerPorEquipo(equipoId));
    }

    @GetMapping("/turno/{turnoId}")
    @Operation(summary = "Listar órdenes por turno", description = "Obtiene todas las órdenes de mantención que se generaron durante un turno específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Órdenes de mantención del turno obtenidas con éxito")
    })
    public ResponseEntity<List<OrdenMantencion>> listarPorTurno(@PathVariable Long turnoId) {
        return ResponseEntity.ok(service.obtenerPorTurno(turnoId)); // Llama al repository.findByTurnoId
    }
}