package com.smushed.market_data.Models.API;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smushed.market_data.Models.API.children.DailyRecord;
import com.smushed.market_data.Models.API.children.TickerMetadata;

public class AlphaTickerTimeSeriesDaily {
    @JsonProperty("Meta Data")
    public TickerMetadata metadata;
    @JsonProperty("Time Series (Daily)")
    public Map<String, DailyRecord> timeSeries;
}
