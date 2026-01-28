package dominik.dembski.lab05.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Globalny handler wyjątków dla REST API.
 * Przechwytuje wyjątki i zwraca odpowiednie odpowiedzi HTTP.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(CpuNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleCpuNotFound(CpuNotFoundException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Not Found");
        body.put("message", ex.getMessage());
        body.put("cpuId", ex.getCpuId());
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }
    
    @ExceptionHandler(DuplicateCpuModelException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateCpuModel(DuplicateCpuModelException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.CONFLICT.value());
        body.put("error", "Conflict");
        body.put("message", ex.getMessage());
        body.put("model", ex.getModel());
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request");
        body.put("message", ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}
