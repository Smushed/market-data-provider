package com.smushed.market_data.Services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.smushed.market_data.Exceptions.TickerSaveException;
import com.smushed.market_data.Models.TickerTimeSeriesDaily;
import com.smushed.market_data.Models.API.AlphaTickerTimeSeriesDaily;
import com.smushed.market_data.Models.API.children.DailyRecord;
import com.smushed.market_data.Utils.DatabaseConnection;

@Service
public class TickersServiceImpl implements TickersService {
    private static final String API_URL = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=IBM&apikey=demo";
    private final DatabaseConnection dbConnection;
    private String insertMarketDayStatement = "INSERT INTO market_days (ticker, trade_date, open_price, close_price, session_high, session_low, volume) " + 
                                                "VALUES (?,?,?,?,?,?,?) " +
                                                "ON CONFLICT (trade_date) DO UPDATE " +
                                                "SET open_price = EXCLUDED.open_price, close_price = EXCLUDED.close_price, " +
                                                "session_high = EXCLUDED.session_high, session_low = EXCLUDED.session_low, volume = EXCLUDED.volume";

    public TickersServiceImpl(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public void SaveTickerData(String ticker) {
        AlphaTickerTimeSeriesDaily apiTickerData = new AlphaTickerTimeSeriesDaily();
        try {
            apiTickerData = fetchTickerData(ticker);
        } catch (Exception err) {
            throw new TickerSaveException("API connection error " + ticker, err);
        }

        Map<String, DailyRecord> timeSeries = apiTickerData.timeSeries;
        List<TickerTimeSeriesDaily> dateRecords = new ArrayList<TickerTimeSeriesDaily>();
        var dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (var entry : timeSeries.entrySet()) {
            var recordDate = LocalDate.parse(entry.getKey(), dateFormatter);
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
        } catch (Exception err) {
            throw new TickerSaveException("Could not save ticker in DB " + ticker, err);
        }
    };

    private AlphaTickerTimeSeriesDaily fetchTickerData(String ticker) {
        var restTemplate = new RestTemplate();
        ResponseEntity<AlphaTickerTimeSeriesDaily> apiTickerTimeSeriesDaily = restTemplate.getForEntity(
            API_URL, AlphaTickerTimeSeriesDaily.class);
        return apiTickerTimeSeriesDaily.getBody();
    }
}
