package cl.duoc.mineria.mantencion.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

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
        } catch (Exception e) {
            System.out.println("[Mantención] Error al conectar con Usuarios (8081). Fallback de desarrollo activo.");
            return true;
        }
    }
}