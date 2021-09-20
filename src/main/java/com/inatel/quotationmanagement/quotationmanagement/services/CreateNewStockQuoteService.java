package com.inatel.quotationmanagement.quotationmanagement.services;

import java.util.Map;
import java.util.Optional;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import com.inatel.quotationmanagement.quotationmanagement.models.Stock;
import com.inatel.quotationmanagement.quotationmanagement.models.StockQuote;
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
    private StockQuotesForm stockQuoteForm;
    private QuoteDateValidator quoteDateValidator;

    public CreateNewStockQuoteService(StockRepository stockRepository, 
                                      StockQuotesRepository stockQuotesRepository, 
                                      StockQuotesForm stockQuotesForm, 
                                      QuoteDateValidator quoteDateValidator) {
        this.stockRepository = stockRepository;
        this.stockQuotesRepository = stockQuotesRepository;
        this.stockQuoteForm = stockQuotesForm;
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
            System.out.println("AFTER VALIDATING QUOTE DATES " + fetchedStockQuote);
            if (fetchedStockQuoteExists) {
                stockQuoteForm.getQuotes().entrySet().forEach(quote ->  {
                    fetchedStockQuote.addToQuotes(quote.getKey(), quote.getValue());
               });
            }
            
            StockQuote stockQuote = fetchedStockQuoteExists 
                                    ? stockQuotesRepository.save(fetchedStockQuote)
                                    : stockQuotesRepository.save(new StockQuote(stockId, formQuotes));
            System.out.println("FETCHED UPDATED: " + stockQuote);
            return stockQuote;
        }
    }

    public void validateQuoteDates(boolean fetchedStockQuoteExists, StockQuote fetchedStockQuote) {
        for (var quote : stockQuoteForm.getQuotes().entrySet()) {
            boolean isValidDate = quoteDateValidator.isValid(quote.getKey());
            if (!isValidDate) { 
                System.out.println("INVALID DATE " + quote.getKey());
                throw new InvalidResourceException();
            }

            if (fetchedStockQuoteExists && fetchedStockQuote.getQuotes().containsKey(quote.getKey())) {
                    System.out.println("REPEATED DATE " + quote.getKey());
                    throw new InvalidResourceException();
            }
        }
    }
}
