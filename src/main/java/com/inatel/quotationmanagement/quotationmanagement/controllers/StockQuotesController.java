package com.inatel.quotationmanagement.quotationmanagement.controllers;

import java.util.List;
import java.util.Optional;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.inatel.quotationmanagement.quotationmanagement.models.StockQuote;
import com.inatel.quotationmanagement.quotationmanagement.repository.StockRepository;
import com.inatel.quotationmanagement.quotationmanagement.models.forms.StockQuotesForm;
import com.inatel.quotationmanagement.quotationmanagement.validation.QuoteDateValidator;
import com.inatel.quotationmanagement.quotationmanagement.repository.StockQuotesRepository;
import com.inatel.quotationmanagement.quotationmanagement.errors.ResourceNotFoundException;
import com.inatel.quotationmanagement.quotationmanagement.services.CreateNewStockQuoteService;

@RestController
@RequestMapping("/stock_quotes")
public class StockQuotesController {

    private StockRepository stockRepository;
    private StockQuotesRepository stockQuotesRepository;
    private QuoteDateValidator quoteDateValidator;

    public StockQuotesController(StockRepository stockRepository, 
                                 StockQuotesRepository stockQuotesRepository,
                                 QuoteDateValidator quoteDateValidator
                                ) {
        this.stockRepository = stockRepository;
        this.stockQuotesRepository = stockQuotesRepository;
        this.quoteDateValidator = quoteDateValidator;
    }
    
    @GetMapping
    @Cacheable("stockQuotes")
    public ResponseEntity<List<StockQuote>> list() {
        List<StockQuote> stockQuotes = stockQuotesRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(stockQuotes);
    }

    @GetMapping("{stockId}")
    public ResponseEntity<StockQuote> findByStockId(@PathVariable("stockId") String stockId) {
        Optional<StockQuote> stockQuote = stockQuotesRepository.findByStockId(stockId);
        if (!stockQuote.isPresent()) {
            throw new ResourceNotFoundException();
        }

        return ResponseEntity.status(HttpStatus.OK).body(stockQuote.get());
    }

    @PostMapping
    @CacheEvict(value = "stockQuotes", allEntries = true)
    public ResponseEntity<StockQuote> createOrUpdate(@RequestBody StockQuotesForm stockQuoteForm) {
        CreateNewStockQuoteService createNewStockQuoteService = new CreateNewStockQuoteService(stockRepository, stockQuotesRepository, stockQuoteForm, quoteDateValidator);
        StockQuote stockQuote = createNewStockQuoteService.execute();

        return ResponseEntity.status(HttpStatus.OK).body(stockQuote);
    }
}