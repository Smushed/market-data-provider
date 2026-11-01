package com.smushed.market_data.Services;

import java.util.List;
import java.util.Map;

import com.smushed.market_data.Models.API.DTO.TickerDailyDto;

public interface TickersService {
    void saveTickerData(String ticker);

    Map<String, List<TickerDailyDto>> pullTickerData(List<String> tickers);
}
