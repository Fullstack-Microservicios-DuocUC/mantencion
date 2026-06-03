package cl.duoc.mineria.mantencion.exception;

import java.time.Instant;
import java.util.stream.Collectors;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    public GlobalExceptionHandler() {
        System.out.println("GlobalExceptionHandler (Módulo 8089) REGISTRADO DE FORMA AUTOMÁTICA");
    }

    //manejo de errores en los DTOs(@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationErrors(MethodArgumentNotValidException ex) {
        System.out.println("[Mantención Error] - Datos de la orden inválidos y omitidos por el cliente");

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST,
            "Error de validación en los datos de la orden de mantención"
        );

        problem.setTitle("Validation Error");
        problem.setProperty("timestamp", Instant.now());

        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
        .collect(Collectors.toMap(
            FieldError::getField,
            error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "Valor inválido"
        ));

        problem.setProperty("errors", errors);
        return problem;
    }

    //maneja errores de JSON deformes o Enum mal escritos
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleJsonParseError(HttpMessageNotReadableException ex) {
        System.out.println("[Mantención Error] Estructura de JSON mal estructurada o Enum inexistente");

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST,
            "Error crítico al deserializar el JSON de mantención. Verifique la sintaxis o los valores de los Enums"
        );

        problem.setTitle("JSON Parse Error");
        problem.setProperty("timestamp", Instant.now());
        problem.setProperty("detalle", ex.getMostSpecificCause().getMessage());
        return problem;
    }

    //maneja error cuando no encuentra el ID de la orden de mantención en la BD
    @ExceptionHandler(MaintenanceNotFoundException.class)
    public ProblemDetail handleMaintenanceNotFound(MaintenanceNotFoundException ex) {
        System.out.println("[Mantención Warning] - Orden solicitada no encontrada: " + ex.getMessage());

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.NOT_FOUND,
            ex.getMessage()
        );

        problem.setTitle("Order not Found");
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneralException(Exception ex) {
        System.out.println("CRÍTICO - Excepción no controlada en el módulo de taller: " + ex.getClass().getName());
        ex.printStackTrace();

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Ocurrió un error inesperado dentro del servidor en el módulo de mantención"
        );

        problem.setTitle("Internal Server Error");
        problem.setProperty("timestamp", Instant.now());
        problem.setProperty("tipoExcepcion", ex.getClass().getSimpleName());
        problem. setProperty("mensajeCorto", ex.getMessage());
        return problem;
    }
}
