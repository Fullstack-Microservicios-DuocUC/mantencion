package cl.duoc.mineria.mantencion.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ordenes_mantencion")
public class OrdenMantencion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "turno_id", nullable = false)
    private Long turnoId;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_equipo", nullable = false, length = 30)
    private TipoEquipo tipoEquipo;

    @Column(name = "equipo_id", nullable = false)
    private Long equipoId;

    @Column(name = "reportado_por_usuario_id", nullable = false)
    private Long reportadoPorUsuarioId;

    @Column(name = "descripcion_falla", nullable = false, columnDefinition = "TEXT")
    private String descripcionFalla;

    @Enumerated(EnumType.STRING)
    @Column(name = "prioridad", nullable = false, length = 20)
    private Prioridad prioridad;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_orden", nullable = false, length = 30)
    private EstadoOrden estadoOrden;

    @Column(name = "fecha_hora_reporte", nullable = false)
    private LocalDateTime fechaHoraReporte;

    @Column(name = "fecha_hora_resolucion")
    private LocalDateTime fechaHoraResolucion;
}
