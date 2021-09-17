package com.inatel.quotationmanagement.quotationmanagement.models.forms;

import java.util.Map;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class StockQuotesForm {
    private String stockId;
    private Map<String, BigDecimal> quotes;
    
    public String getStockId() {
        return this.stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public Map<String, BigDecimal> getQuotes() {
        return this.quotes;
    }

    public void setQuotes(Map<String, BigDecimal> quotes) {
        this.quotes = quotes;
    }
}