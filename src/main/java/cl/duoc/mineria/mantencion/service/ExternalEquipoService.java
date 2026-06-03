package cl.duoc.mineria.mantencion.service;

import cl.duoc.mineria.mantencion.model.TipoEquipo;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ExternalEquipoService {

    private final WebClient webClient = WebClient.create();

    public boolean verificarEquipoExiste(TipoEquipo tipo, Long equipoId) {
        if (tipo == null || equipoId == null) return false;

        try {
            if (tipo == TipoEquipo.CAMION) {
                // Consulta directa al puerto de Camiones (8084)
                Boolean existe = webClient.get()
                        .uri("http://localhost:8084/api/v1/camiones/existe/" + equipoId)
                        .retrieve()
                        .bodyToMono(Boolean.class)
                        .block();
                return existe != null && existe;

            } else if (tipo == TipoEquipo.PALA) {
                // Consulta directa al puerto de Palas (8085)
                Boolean existe = webClient.get()
                        .uri("http://localhost:8085/api/v1/palas/existe/" + equipoId)
                        .retrieve()
                        .bodyToMono(Boolean.class)
                        .block();
                return existe != null && existe;
            }
        } catch (Exception e) {
            System.out.println("[Mantención] Error de conexión con el microservicio de " + tipo + ". Fallback activo.");
            return true; // Fallback temporal para desarrollo local si el módulo está apagado
        }
        return false;
    }
}