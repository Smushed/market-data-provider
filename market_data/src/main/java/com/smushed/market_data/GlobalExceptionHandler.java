package com.smushed.market_data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.smushed.market_data.Exceptions.TickerSaveException;

@RestControllerAdvice 
public class GlobalExceptionHandler {
    Logger log = LoggerFactory.getLogger(ExceptionHandler.class);

    @ExceptionHandler(TickerSaveException.class)
    public ResponseEntity<String> handleTickerSave(TickerSaveException ex) {
        log.error("Ticker Save Failed. Message: " + ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Ticker could not be saved");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> genericErrorHandler(Exception ex) {
        log.error("Unexpected Error. Message : " + ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Ticker could not be saved");
    }
}
