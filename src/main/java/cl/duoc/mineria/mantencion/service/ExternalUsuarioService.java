package cl.duoc.mineria.mantencion.service;

import cl.duoc.mineria.mantencion.exception.ServicioExternoNoDisponibleException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class ExternalUsuarioService {

    private final WebClient webClient = WebClient.create();

    public boolean verificarUsuarioExiste(Long usuarioId) {
        try {
            Boolean existe = webClient.get()
                    .uri("http://localhost:8081/api/v1/usuarios/existe/" + usuarioId)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
            return existe != null && existe;
        } catch (WebClientResponseException.NotFound e) {
            return false;
        } catch (Exception e) {
            throw new ServicioExternoNoDisponibleException(
                "No se pudo validar el usuario " + usuarioId + ": " + e.getMessage());
        }
    }
}