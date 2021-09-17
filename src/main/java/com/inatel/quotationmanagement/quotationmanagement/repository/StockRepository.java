package com.inatel.quotationmanagement.quotationmanagement.repository;

import java.util.List;
import java.util.Arrays;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.cache.annotation.Cacheable;
import org.hibernate.annotations.Cache;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.transaction.annotation.Transactional;
import com.inatel.quotationmanagement.quotationmanagement.models.Stock;

@Repository
public class StockRepository {

    private String url = "http://localhost:8080/stock/";

    @Cacheable(value = "stocks")
    public List<Stock> getAllStocks() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Stock[]> response = restTemplate.getForEntity(this.url, Stock[].class);
        List<Stock> stocks = Arrays.asList(response.getBody());
        return stocks;
    }

    @CachePut(value="stocks", key="#stockId")
    public Stock getStock(String stockId) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Stock> response = restTemplate.getForEntity(this.url + stockId, Stock.class);
        Stock stock = response.getBody();

        return stock;
    }

    @Transactional
    @CacheEvict(value = "stocks", allEntries = true)
    public void addStock(Stock stock) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForEntity(this.url, stock, Stock.class);
    }
}