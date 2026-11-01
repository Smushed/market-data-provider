package com.smushed.market_data.Exceptions;

public class TickerSaveException extends RuntimeException {
    public TickerSaveException(String message, Throwable cause) {
        super(message, cause);
    }

    public TickerSaveException(String message) {
        super(message);
    }
}
