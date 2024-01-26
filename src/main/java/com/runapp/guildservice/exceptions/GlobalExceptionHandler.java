package com.runapp.guildservice.exceptions;

import com.runapp.guildservice.dto.response.DeleteResponse;
import com.runapp.guildservice.dto.response.ExceptionResponse;
import com.runapp.guildservice.exceptions.ImageDoesntExistException;
import com.runapp.guildservice.exceptions.TeamBadRequestException;
import com.runapp.guildservice.exceptions.TeamNotFoundException;
import com.runapp.guildservice.exceptions.UserTeamNotFoundException;
import feign.FeignException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(TeamNotFoundException.class)
    public ResponseEntity<Object> handleTeamNotFoundException(TeamNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
    @ExceptionHandler(FeignException.NotFound.class)
    public ResponseEntity<Object> handleFeignExceptionNotFound(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ExceptionResponse(LocalDateTime.now(), "Story or Admin not found"));
    }

    @ExceptionHandler(FeignException.InternalServerError.class)
    public ResponseEntity<Object> handleFeignInternalServerError(FeignException.InternalServerError e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionResponse(LocalDateTime.now(), "No such history or user exists"));
    }
    @ExceptionHandler(TeamBadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(TeamBadRequestException e){
        return ResponseEntity.badRequest().body(
                new ExceptionResponse(LocalDateTime.now(),
                        e.getMessage()));
    }

    @ExceptionHandler(UserTeamNotFoundException.class)
    public ResponseEntity<Object> handleUserTeamNotFoundException(UserTeamNotFoundException e){
        return ResponseEntity.badRequest().body(
                new ExceptionResponse(LocalDateTime.now(),
                        e.getMessage()));
    }
    @ExceptionHandler(ImageDoesntExistException.class)
    public ResponseEntity<Object> handleImageDoesntExistException(ImageDoesntExistException e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DeleteResponse(e.getMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        List<String> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(this::getErrorMessage)
                .collect(Collectors.toList());
        body.put("errors", errors);
        return new ResponseEntity<>(body, headers, status);
    }

    private String getErrorMessage(ObjectError e) {
        if (e instanceof FieldError) {
            String field = ((FieldError) e).getField();
            return field + " " + e.getDefaultMessage();
        }
        return e.getDefaultMessage();
    }

}
