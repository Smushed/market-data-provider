package com.smushed.market_data.Exceptions;

public class TickerPullException extends RuntimeException {
    public TickerPullException(String message, Throwable cause) {
        super(message, cause);
    }
}
