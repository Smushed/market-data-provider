package com.smushed.market_data.Models.API.children;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TickerMetadata {
    @JsonProperty("1. Information")
    public String information;
    @JsonProperty("2. Symbol")
    public String symbol;
    @JsonProperty("3. Last Refreshed")
    public String lastRefreshed;
    @JsonProperty("4. Output Size")
    public String outputSize;
    @JsonProperty("5. Time Zone")
    public String timeZone;
}
