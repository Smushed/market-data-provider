package com.smushed.market_data.Routes;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<Void> getTickerData(@PathVariable String ticker) {
        tickersService.SaveTickerData(ticker);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}   
