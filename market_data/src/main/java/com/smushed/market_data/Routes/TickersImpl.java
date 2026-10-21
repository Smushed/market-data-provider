package com.smushed.market_data.Routes;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.smushed.market_data.Models.API.AlphaTickerTimeSeriesDaily;
import com.smushed.market_data.Models.API.children.DailyRecord;
import com.smushed.market_data.Utils.DatabaseConnection;
import com.smushed.market_data.Models.TickerTimeSeriesDaily;

@RestController 
@RequestMapping("/api/tickers")
public class TickersImpl {
    private static final String API_URL = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=IBM&apikey=demo";
    private final DatabaseConnection dbConnection;

    public TickersImpl(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @PostMapping("/{ticker}") 
    public String getTickerData(@PathVariable String ticker) {
        var restTemplate = new RestTemplate();
        ResponseEntity<AlphaTickerTimeSeriesDaily> apiTickerTimeSeriesDaily = restTemplate.getForEntity(
            API_URL, AlphaTickerTimeSeriesDaily.class);

        final var apiTickerTimeSeriesBody = apiTickerTimeSeriesDaily.getBody();

        Map<String, DailyRecord> timeSeries = apiTickerTimeSeriesBody.timeSeries;
        List<TickerTimeSeriesDaily> dateRecords = new ArrayList<TickerTimeSeriesDaily>();
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (var entry : timeSeries.entrySet()) {
            var recordDate = LocalDate.parse(entry.getKey(), formatter);
            final var dailyRecord = entry.getValue();
            var record = new TickerTimeSeriesDaily(ticker, recordDate, dailyRecord.open, dailyRecord.close, 
                dailyRecord.high, dailyRecord.low, dailyRecord.volume);
            dateRecords.add(record);
        }
        
        Connection conn = dbConnection.getConnection();
        // var tickerTimeSeriesDaily = new TickerTimeSeriesDaily(apiTickerTimeSeriesBody.metadata.symbol,
        //     null, 0, 0, 0, 0)
        return "Hello world";
    }
}   
