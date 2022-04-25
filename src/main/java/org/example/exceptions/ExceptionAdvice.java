package org.example.exceptions;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;

@ControllerAdvice
public class ExceptionAdvice{

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handleAllDataAccessExceptions(DataAccessException exception){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
    }

    @ExceptionHandler(UrlNotFoundException.class)
    public ResponseEntity<String> handleUrlNotFoundException(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(TokenGenerationException.class)
    public ResponseEntity<String> handleTokenGenerationException(TokenGenerationException exception){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
    }

    @ExceptionHandler(RequestValidationException.class)
    public ResponseEntity<?> handleRequestValidationException(RequestValidationException exception){
        return ResponseEntity.badRequest().body(exception.getErrors());
    }

    //todo
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException exception){
        Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
        //todo transform to pojo
        Map<String, List<String>> errors = new HashMap<>();
        violations.forEach(violation -> {
            String field = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            setErrors(errors, field, message);
        });
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        Map<String, List<String>> errors = new HashMap<>();
        exception.getFieldErrors().forEach(e -> {
            String field = e.getField();
            String message = e.getDefaultMessage();
            setErrors(errors, field, message);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    private void setErrors(Map<String, List<String>> errors, String field, String message) {
        List<String> messageList = errors.get(field);
        if (messageList == null){
            ArrayList<String> newMessages = new ArrayList<>();
            newMessages.add(message);
            errors.put(field, newMessages);
        } else {
            messageList.add(message);
        }
    }


}
