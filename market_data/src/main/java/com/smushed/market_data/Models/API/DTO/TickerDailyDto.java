package com.smushed.market_data.Models.API.DTO;

import java.sql.Date;

public record TickerDailyDto(
    Date tradeDate,
    float open,
    float close,
    float high,
    float low,
    int volume
) {};
