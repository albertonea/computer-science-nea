package com.eastbarnetschool.ordermatchingengine.api.controller.rest;

import com.eastbarnetschool.ordermatchingengine.api.model.TimeInterval;
import com.eastbarnetschool.ordermatchingengine.api.model.dto.CandlestickDto;
import com.eastbarnetschool.ordermatchingengine.api.service.TradeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/trade-history")
public class TradeHistoryController {
    private final TradeService tradeService;

    public TradeHistoryController(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    @GetMapping("/{ticker}")
    public ResponseEntity<List<CandlestickDto>> getCandlesticks(@PathVariable String ticker, @RequestParam("interval") TimeInterval timeInterval) {
        Optional<List<CandlestickDto>> optionalCandlesticks = tradeService.getCandlesticks(ticker, timeInterval);

        if (optionalCandlesticks.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<CandlestickDto> candlesticks = optionalCandlesticks.get();
        return ResponseEntity.ok(candlesticks);
    }
}
