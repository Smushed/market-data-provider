package com.smushed.market_data.Routes;

import org.springframework.http.ResponseEntity;

public interface Tickers {
    public ResponseEntity<String> getTickerData();
}
