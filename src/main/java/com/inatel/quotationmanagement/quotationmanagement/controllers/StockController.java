package com.inatel.quotationmanagement.quotationmanagement.controllers;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.inatel.quotationmanagement.quotationmanagement.models.Stock;
import com.inatel.quotationmanagement.quotationmanagement.repository.StockRepository;
import com.inatel.quotationmanagement.quotationmanagement.errors.ResourceAlreadyExistsException;

@RestController
@RequestMapping("/stocks")
public class StockController {
    private StockRepository stockRepository;

    public StockController(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @GetMapping
    public ResponseEntity<List<Stock>> list() {
        List<Stock> stocks = this.stockRepository.getAllStocks();
    
        return ResponseEntity.status(HttpStatus.OK).body(stocks);
    }

    @PostMapping
    public ResponseEntity<Stock> create(@RequestBody Stock newStock) {
        Stock fetchedStock = this.stockRepository.getStock(newStock.getId());

        if (fetchedStock == null) {
            this.stockRepository.addStock(newStock);
            return ResponseEntity.status(HttpStatus.OK).body(newStock);
        } else {
            throw new ResourceAlreadyExistsException();
        }
    }
}