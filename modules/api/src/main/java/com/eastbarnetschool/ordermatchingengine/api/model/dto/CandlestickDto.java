package com.eastbarnetschool.ordermatchingengine.api.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class CandlestickDto {
    private String ticker;
    private Timestamp intervalStart;
    private long open;
    private long high;
    private long low;
    private long close;
    private long volume;

}
