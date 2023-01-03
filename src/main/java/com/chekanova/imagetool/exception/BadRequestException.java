package com.chekanova.imagetool.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class BadRequestException extends RuntimeException {
    @Getter
    private final HttpStatus httpStatus;

    public BadRequestException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}

