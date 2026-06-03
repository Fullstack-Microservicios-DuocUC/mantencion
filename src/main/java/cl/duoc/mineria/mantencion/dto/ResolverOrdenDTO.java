package cl.duoc.mineria.mantencion.dto;

import cl.duoc.mineria.mantencion.model.EstadoOrden;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResolverOrdenDTO {

    @NotNull(message = "El ID de la orden de mantención es obligatorio")
    @Min(value = 1, message = "El ID de la orden debe ser mayor que 0")
    private Long id;

    @NotNull(message = "El nuevo estado de la orden es obligatorio (e.g COMPLETADA, RECHAZADA)")
    private EstadoOrden estadoOrden;
}
