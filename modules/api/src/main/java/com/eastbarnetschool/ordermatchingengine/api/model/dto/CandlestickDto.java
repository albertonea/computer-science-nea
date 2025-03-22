package com.eastbarnetschool.ordermatchingengine.api.model.dto;

import java.sql.Timestamp;

public class CandlestickDto {
    private String ticker;
    private Timestamp intervalStart;
    private long open;
    private long high;
    private long low;
    private long close;
    private long volume;

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public void setIntervalStart(Timestamp intervalStart) {
        this.intervalStart = intervalStart;
    }

    public void setOpen(long open) {
        this.open = open;
    }

    public void setHigh(long high) {
        this.high = high;
    }

    public void setLow(long low) {
        this.low = low;
    }

    public void setClose(long close) {
        this.close = close;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public String getTicker() {
        return ticker;
    }

    public Timestamp getIntervalStart() {
        return intervalStart;
    }

    public long getOpen() {
        return open;
    }

    public long getHigh() {
        return high;
    }

    public long getLow() {
        return low;
    }

    public long getClose() {
        return close;
    }

    public long getVolume() {
        return volume;
    }
}
