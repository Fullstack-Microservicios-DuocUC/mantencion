package cl.duoc.mineria.mantencion.exception;

public class MaintenanceNotFoundException extends RuntimeException {
    
    public MaintenanceNotFoundException(String mensaje) {
        super(mensaje);
    }
}
