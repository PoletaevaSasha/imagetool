package com.chekanova.imagetool.validation;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Class for creating specific error report, which will be sent to client in response
 *
 * @author Natanius18
 */
@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler({IOException.class, InterruptedException.class, IllegalArgumentException.class})
    protected ResponseEntity<Object> handleConflict(RuntimeException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", ex.getMessage());
        return new ResponseEntity<>(body, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}
