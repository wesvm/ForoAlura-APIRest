package com.foro.api.errors;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandling {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity error404(){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity error400(MethodArgumentNotValidException e){
        var errors = e.getFieldErrors().stream().map(dataErrorValidation::new).toList();

        return ResponseEntity.badRequest().body(errors);
    }

    private record dataErrorValidation(String field, String error){
        public dataErrorValidation(FieldError error){
            this(error.getField(), error.getDefaultMessage());
        }
    }

}
