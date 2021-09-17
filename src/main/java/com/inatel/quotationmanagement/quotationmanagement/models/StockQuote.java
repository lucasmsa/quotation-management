package com.inatel.quotationmanagement.quotationmanagement.models;

import java.util.Map;
import java.math.BigDecimal;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

@Entity
@Table(name = "stock_quotes")
public class StockQuote {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @Column(unique=true)
    private String stockId;

    @ElementCollection
    @CollectionTable(name="stock_quote_mapping", joinColumns = {@JoinColumn(name = "stock_quote_id", referencedColumnName = "id")})
    @MapKeyColumn(name = "quote_date")
    @Column(name="value")
    private Map<String, BigDecimal> quotes;

    public StockQuote(String stockId, Map<String, BigDecimal> formQuotes) {
        this.stockId = stockId;
        this.quotes = formQuotes;
    }

    public StockQuote() {}

    public String getId() {
        return id;
    }

    public String getStockId() {
        return stockId;
    }

    public Map<String, BigDecimal> getQuotes() {
        return quotes;
    }

    public void addToQuotes(String quoteDate, BigDecimal value) {
        quotes.put(quoteDate, value);
    } 
}
