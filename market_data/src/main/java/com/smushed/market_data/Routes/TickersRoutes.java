package com.smushed.market_data.Routes;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public interface TickersRoutes {
    public ResponseEntity<Void> getTickerData(@PathVariable String ticker);
}
