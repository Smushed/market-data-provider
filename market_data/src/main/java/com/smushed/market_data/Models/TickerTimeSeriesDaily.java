package com.smushed.market_data.Models;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor 
public class TickerTimeSeriesDaily {
    public String ticker;
    public LocalDate tradeDate;
    public float open;
    public float close;
    public float high;
    public float low;
    public int volume;
}
