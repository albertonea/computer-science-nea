package com.eastbarnetschool.ordermatchingengine.api.controller.rest;

import com.eastbarnetschool.ordermatchingengine.api.model.dto.TradeDto;
import com.eastbarnetschool.ordermatchingengine.api.model.dto.TradeHistoryResponseDto;
import com.eastbarnetschool.ordermatchingengine.api.service.TradeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/trade-history")
public class TradeHistoryController {
    private final TradeService tradeService;

    public TradeHistoryController(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    @GetMapping("/{ticker}")
    public ResponseEntity<List<TradeHistoryResponseDto>> getTradeHistory(@PathVariable String ticker) {
        return ResponseEntity.ok(tradeService.getTradeHistory(ticker));
    }

}
