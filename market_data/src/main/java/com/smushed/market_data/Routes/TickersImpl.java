package com.smushed.market_data.Routes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
    private String insertMarketDayStatement = "INSERT INTO market_days (ticker, trade_date, open_price, close_price, session_high, session_low, volume) " + 
                                                "VALUES (?,?,?,?,?,?,?) " +
                                                "ON CONFLICT (trade_date) DO UPDATE " +
                                                "SET open_price = EXCLUDED.open_price, close_price = EXCLUDED.close_price, " +
                                                "session_high = EXCLUDED.session_high, session_low = EXCLUDED.session_low, volume = EXCLUDED.volume";

    public TickersImpl(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @PostMapping("/{ticker}") 
    public ResponseEntity<String> getTickerData(@PathVariable String ticker) {
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
            TickerTimeSeriesDaily record = new TickerTimeSeriesDaily(ticker, recordDate, dailyRecord.open, dailyRecord.close, 
                dailyRecord.high, dailyRecord.low, dailyRecord.volume);
            dateRecords.add(record);
        }
        
        try (Connection conn = dbConnection.getConnection();
            PreparedStatement statement = conn.prepareStatement(insertMarketDayStatement, Statement.RETURN_GENERATED_KEYS)) {
            for (var rec : dateRecords) {
                statement.setString(1, rec.getTicker());
                statement.setDate(2, java.sql.Date.valueOf(rec.getTradeDate()));
                statement.setFloat(3, rec.getOpen());
                statement.setFloat(4, rec.getClose());
                statement.setFloat(5, rec.getHigh());
                statement.setFloat(6, rec.getLow());
                statement.setInt(7, rec.getVolume());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException err) {
            System.out.println("Database connection not established");
            return ResponseEntity.internalServerError().body("Connection could not be established");
        }
        // var tickerTimeSeriesDaily = new TickerTimeSeriesDaily(apiTickerTimeSeriesBody.metadata.symbol,
        //     null, 0, 0, 0, 0)
        return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.CREATED);
    }
}   
