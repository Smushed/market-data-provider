package com.smushed.market_data.Routes;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import com.smushed.market_data.Models.API.DTO.TickerDailyDto;

public interface TickersRoutes {
    public ResponseEntity<Void> getAndSaveTickerData(@PathVariable String ticker);

    public ResponseEntity<Map<String, List<TickerDailyDto>>> pullTickerData(@PathVariable List<String> tickers);
}
