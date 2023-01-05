package com.chekanova.imagetool.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler({AccessDeniedException.class})
  public ResponseEntity<Object> handleAccessDeniedException(
      AccessDeniedException exception, WebRequest request) {
    return buildResponse(exception, HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler({ConstraintViolationException.class, BadRequestException.class})
  public ResponseEntity<Object> handleConstraintViolationException(
      Exception exception, WebRequest request) {
    return buildResponse(exception, HttpStatus.BAD_REQUEST);
  }

  private ResponseEntity<Object> buildResponse(Exception exception, HttpStatus status) {
    return new ResponseEntity<>(
        Map.of("timestamp", new Date(), "status", status, "error", exception.getMessage()),
        new HttpHeaders(),
        status);
  }
}
