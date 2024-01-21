package com.runapp.guildservice.advice;

import com.runapp.guildservice.dto.response.DeleteResponse;
import com.runapp.guildservice.dto.response.ExceptionResponse;
import com.runapp.guildservice.dto.response.ValidationErrorResponse;
import com.runapp.guildservice.exceptions.ImageDoesntExistException;
import com.runapp.guildservice.exceptions.TeamBadRequestException;
import com.runapp.guildservice.exceptions.TeamNotFoundException;
import com.runapp.guildservice.exceptions.UserTeamNotFoundException;
import feign.FeignException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GuildExceptionHandler {
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
    public ResponseEntity<Object> handleTeamNotFoundException(TeamBadRequestException e){
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
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(errors);
    }

}
