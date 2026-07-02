package cl.duoc.mineria.mantencion.service;

import cl.duoc.mineria.mantencion.exception.ServicioExternoNoDisponibleException;
import cl.duoc.mineria.mantencion.model.TipoEquipo;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class ExternalEquipoService {

    private final WebClient webClient;

    public ExternalEquipoService(WebClient webClient) {
        this.webClient = webClient;
    }
    public boolean verificarEquipoExiste(TipoEquipo tipo, Long equipoId) {
        if (tipo == null || equipoId == null) return false;

        try {
            if (tipo == TipoEquipo.CAMION) {
                // Consulta directa al puerto de Camiones (8084)
                Boolean existe = webClient.get()
                        .uri("http://camiones/api/v1/camiones/existe/" + equipoId)
                        .retrieve()
                        .bodyToMono(Boolean.class)
                        .block();
                return existe != null && existe;

            } else if (tipo == TipoEquipo.PALA) {
                // Consulta directa al puerto de Palas (8083)
                Boolean existe = webClient.get()
                        .uri("http://palas/api/v1/palas/existe/" + equipoId)
                        .retrieve()
                        .bodyToMono(Boolean.class)
                        .block();
                return existe != null && existe;
            }
        } catch (WebClientResponseException.NotFound e) {
            // El otro servicio respondió: "este equipo no existe". Esa respuesta sí es confiable.
            return false;
        } catch (Exception e) {
            // Cualquier otro problema (timeout, conexión rechazada, error 500, etc):
            // no sabemos si el equipo existe o no, así que NO asumimos que sí. Se detiene la operación.
            throw new ServicioExternoNoDisponibleException(
                "No se pudo validar el equipo " + tipo + " con ID " + equipoId + ": " + e.getMessage());
        }
        return false;
    }
}