package com.Gym.Exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class GlobalHandlerException{

    @ExceptionHandler(MethodParameterNull.class)
    public ResponseEntity<String> handleMethodParameterNull(MethodParameterNull ex) {
        log.warn("Parameter invalid on req : {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }


    @ExceptionHandler(ObjectNotFound.class)
    public ResponseEntity<String> handleObjectNotFound(ObjectNotFound ex) {
        log.warn("Resource not found in database: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleBadJsonFormat(MethodArgumentNotValidException ex) {
        Map<String, String> erros = new HashMap<>();


        ex.getBindingResult().getFieldErrors().forEach(error ->
                erros.put(error.getField(), error.getDefaultMessage())
        );
        log.warn("bad json format : {}", erros);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erros);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleValidationExceptions(HttpMessageNotReadableException ex) {
        log.warn("Malformed JSON request body: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> integrityViolationException(DataIntegrityViolationException ex) {
        Throwable rootCause = ex.getRootCause();
        String errorMessage = "Error of integrity of database";

        if (rootCause != null) {
            String message = rootCause.getMessage();

            // Se for erro de nulo (Erro 1048 no MySQL)
            if (message.contains("cannot be null")) {
                errorMessage = "Validation erro : " + message;
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage); // Retorna 400 se faltar dado
            }

            if (message.contains("Duplicate entry")) {
                errorMessage = "Data conflict : " + message;
                return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage); // Mantém o 409 apenas para duplicados
            }

            errorMessage = "Restriction violation : " + message;
        }
        log.error("Database integrity error: {}", errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDenied(AccessDeniedException ex) {
        log.warn("Access denied: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You dont have the permission to use this");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {

        log.error("Unexpected internal server error: ", ex);
        return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected internal server error occurred.");
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentials(BadCredentialsException ex) {
        log.warn("Bad credentials login attempt: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }
}
