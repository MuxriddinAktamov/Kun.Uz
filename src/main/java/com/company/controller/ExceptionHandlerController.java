package com.company.controller;

import com.company.exceptions.BadRequestException;
import com.company.exceptions.ForbiddenException;
import com.company.exceptions.ItemNotFoundException;
import com.company.exceptions.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {
    @ExceptionHandler({ItemNotFoundException.class, BadRequestException.class})
    public ResponseEntity<?> handleException(RuntimeException ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler({ForbiddenException.class})
    public ResponseEntity<?> handleException(ForbiddenException ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler({UnauthorizedException.class})
    public ResponseEntity<?> handleException(UnauthorizedException ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
