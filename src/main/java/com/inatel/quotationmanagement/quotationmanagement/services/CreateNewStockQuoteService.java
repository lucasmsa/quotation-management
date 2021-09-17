package com.inatel.quotationmanagement.quotationmanagement.services;

import java.util.Map;
import java.util.Optional;
import java.math.BigDecimal;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import com.inatel.quotationmanagement.quotationmanagement.models.Quote;
import com.inatel.quotationmanagement.quotationmanagement.models.Stock;
import com.inatel.quotationmanagement.quotationmanagement.models.StockQuote;
import com.inatel.quotationmanagement.quotationmanagement.repository.QuoteRepository;
import com.inatel.quotationmanagement.quotationmanagement.repository.StockRepository;
import com.inatel.quotationmanagement.quotationmanagement.models.forms.StockQuotesForm;
import com.inatel.quotationmanagement.quotationmanagement.validation.QuoteDateValidator;
import com.inatel.quotationmanagement.quotationmanagement.errors.InvalidResourceException;
import com.inatel.quotationmanagement.quotationmanagement.repository.StockQuotesRepository;
import com.inatel.quotationmanagement.quotationmanagement.errors.ResourceNotFoundException;

@Service
public class CreateNewStockQuoteService {

    private StockRepository stockRepository;
    private StockQuotesRepository stockQuotesRepository;
    private QuoteRepository quoteRepository;
    private StockQuotesForm stockQuoteForm;
    private CacheManager cacheManager;
    private QuoteDateValidator quoteDateValidator;

    public CreateNewStockQuoteService(StockRepository stockRepository, 
                                      StockQuotesRepository stockQuotesRepository, 
                                      QuoteRepository quoteRepository, 
                                      StockQuotesForm stockQuotesForm, 
                                      CacheManager cacheManager,
                                      QuoteDateValidator quoteDateValidator) {
        this.stockRepository = stockRepository;
        this.stockQuotesRepository = stockQuotesRepository;
        this.quoteRepository = quoteRepository;
        this.stockQuoteForm = stockQuotesForm;
        this.cacheManager = cacheManager;
        this.quoteDateValidator = quoteDateValidator;
    }

    public StockQuote execute() {
        String stockId = stockQuoteForm.getStockId();
        
        Stock fetchedStock = stockRepository.getStock(stockId);
        if (fetchedStock == null) {
            throw new ResourceNotFoundException();
        } else {
            Map<String, BigDecimal> formQuotes = stockQuoteForm.getQuotes();
            Optional<StockQuote> optionalStockQuote = stockQuotesRepository.findByStockId(stockId);
            boolean fetchedStockQuoteExists = optionalStockQuote.isPresent();
            StockQuote fetchedStockQuote = fetchedStockQuoteExists ? optionalStockQuote.get() : null;

            this.validateQuoteDates(fetchedStockQuoteExists, fetchedStockQuote);
            
            stockQuoteForm.getQuotes().entrySet().forEach(quote ->  {
                if (fetchedStockQuoteExists) fetchedStockQuote.addToQuotes(quote.getKey(), quote.getValue());
                Quote newQuote = new Quote(quote.getKey(), quote.getValue());
                quoteRepository.save(newQuote);
            });
           
            StockQuote stockQuote = fetchedStockQuoteExists 
                                    ? stockQuotesRepository.save(fetchedStockQuote)
                                    : stockQuotesRepository.save(new StockQuote(stockId, formQuotes));
            return stockQuote;
        }
    }

    private void validateQuoteDates(boolean fetchedStockQuoteExists, StockQuote fetchedStockQuote) {
        for (var quote : stockQuoteForm.getQuotes().entrySet()) {
            boolean isValidDate = quoteDateValidator.isValid(quote.getKey());
            if (!isValidDate) throw new InvalidResourceException();

            if (fetchedStockQuoteExists && fetchedStockQuote.getQuotes().containsKey(quote.getKey())) {
                    throw new InvalidResourceException();
            }
        }
    }
}