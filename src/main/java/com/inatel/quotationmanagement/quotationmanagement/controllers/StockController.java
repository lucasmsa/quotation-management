package com.inatel.quotationmanagement.quotationmanagement.controllers;

import java.util.List;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inatel.quotationmanagement.quotationmanagement.errors.ResourceAlreadyExists;
import com.inatel.quotationmanagement.quotationmanagement.models.Stock;
import com.inatel.quotationmanagement.quotationmanagement.repository.StockRepository;

@RestController
@RequestMapping("/stocks")
public class StockController {

    @Autowired
    private StockRepository stockRepository;

    @GetMapping
    public ResponseEntity<List<Stock>> list() {
        List<Stock> stocks = stockRepository.getAllStocks();
        return ResponseEntity.status(HttpStatus.OK).body(stocks);
    }

    @PostMapping
    public ResponseEntity<Stock> create(@RequestBody Stock newStock) {
        Stock fetchedStock = stockRepository.getStock(newStock.getId());
        if (fetchedStock == null) {
            stockRepository.addStock(newStock);
            return ResponseEntity.status(HttpStatus.OK).body(newStock);
        } else {
            throw new ResourceAlreadyExists();
        }
    }
}