package com.smushed.market_data.Routes;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smushed.market_data.Models.TickerTimeSeriesDaily;
import com.smushed.market_data.Models.API.DTO.TickerDailyDto;
import com.smushed.market_data.Services.TickersService;

@RestController 
@RequestMapping("/api/tickers")
public class TickersRoutesImpl implements TickersRoutes {
    private final TickersService tickersService;

    public TickersRoutesImpl(TickersService tickersService) {
        this.tickersService = tickersService;
    }

    @PostMapping("/{ticker}") 
    @Override
    public ResponseEntity<Void> getAndSaveTickerData(@PathVariable String ticker) {
        tickersService.saveTickerData(ticker);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{tickers}")
    @Override
    public ResponseEntity<Map<String, List<TickerDailyDto>>> pullTickerData(@PathVariable List<String> tickers) {
        Map<String, List<TickerDailyDto>> response = tickersService.pullTickerData(tickers);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}   
