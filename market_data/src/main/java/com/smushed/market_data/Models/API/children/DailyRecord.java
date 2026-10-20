package com.smushed.market_data.Models.API.children;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DailyRecord {
    @JsonProperty("1. open")
    public float open;
    @JsonProperty("2. high")
    public float high;
    @JsonProperty("3. low")
    public float low;
    @JsonProperty("4. close")
    public float close;
    @JsonProperty("5. volume")
    public int volume;
}
