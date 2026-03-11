package com.periferia.taskApp.util;

import com.periferia.taskApp.dto.ResponseDTO;
import com.periferia.taskApp.dto.TaskResponse;
import com.periferia.taskApp.exceptions.NoExitsElementException;
import com.periferia.taskApp.exceptions.PaginateException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO<?>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return resultErrorValidations(errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseDTO<?>> handleConstraintViolationExceptions(
            ConstraintViolationException ex) {

        Map<String, String> errors = new HashMap<>();

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String propertyPath = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            errors.put(propertyPath, message);
        }

        return resultErrorValidations(errors);
    }

    private ResponseEntity<ResponseDTO<?>> resultErrorValidations(Map<String, String> errors) {
        ResponseDTO<?> responseDTO = ResponseDTO.builder()
                .code(400)
                .message("Error de validación")
                .data(errors)
                .build();

        return ResponseEntity.badRequest().body(responseDTO);
    }

    @ExceptionHandler({NoExitsElementException.class , PaginateException.class })
    public ResponseEntity<ResponseDTO<?>> handleNoExistsElement(RuntimeException ex) {
        ResponseDTO<?> responseDTO = ResponseDTO.<TaskResponse>builder()
                .code(400)
                .message(ex.getMessage())
                .build();
        return ResponseEntity.badRequest().body(responseDTO);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseDTO<?>> handleGenericRuntimeException(RuntimeException ex) {
        ResponseDTO<?> responseDTO = ResponseDTO.<TaskResponse>builder()
                .code(500)
                .message("Error interno en el servidor")
                .error(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseDTO);
    }
}
