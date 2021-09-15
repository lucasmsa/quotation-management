package com.inatel.quotationmanagement.quotationmanagement.controllers;

import java.util.List;
import java.util.Arrays;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.inatel.quotationmanagement.quotationmanagement.models.Stock;

@RestController
@RequestMapping("/stocks")
public class StockController {
    @GetMapping
    public List<Stock> list() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Stock[]> response = restTemplate.getForEntity("http://localhost:8080/stock", Stock[].class);
        List<Stock> stocks = Arrays.asList(response.getBody());
        return stocks;
    }
}
