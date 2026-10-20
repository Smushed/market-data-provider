package com.smushed.market_data.Routes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.smushed.market_data.Models.API.AlphaTickerTimeSeriesDaily;
import com.smushed.market_data.Models.TickerTimeSeriesDaily;

@RestController 
@RequestMapping("/api/tickers")
public class TickersImpl {
    private static String API_URL = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=IBM&apikey=demo";


    @PostMapping("/{ticker}") 
    public String getTickerData(@PathVariable String ticker) {
        var restTemplate = new RestTemplate();
        ResponseEntity<AlphaTickerTimeSeriesDaily> apiTickerTimeSeriesDaily = restTemplate.getForEntity(
            API_URL, AlphaTickerTimeSeriesDaily.class);

        final var apiTickerTimeSeriesBody = apiTickerTimeSeriesDaily.getBody();

        var timeSeries = apiTickerTimeSeriesBody.timeSeries;
        List<TickerTimeSeriesDaily> dateRecords;
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (var entry : timeSeries.entrySet()) {
            var recordDate = LocalDate.parse(entry.getKey(), formatter);
            final var dailyRecord = entry.getValue();
            var record = new TickerTimeSeriesDaily(ticker, recordDate, dailyRecord.open, dailyRecord.close, 
                dailyRecord.high, dailyRecord.low);
                var something = "";
            // dateRecords.add(new TickerTimeSeriesDaily(entry.getValue()))
        }
        
        // var tickerTimeSeriesDaily = new TickerTimeSeriesDaily(apiTickerTimeSeriesBody.metadata.symbol,
        //     null, 0, 0, 0, 0)
        return "Hello world";
    }
}   
