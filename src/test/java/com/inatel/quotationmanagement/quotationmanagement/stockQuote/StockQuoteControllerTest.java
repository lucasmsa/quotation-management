package com.inatel.quotationmanagement.quotationmanagement.stockQuote;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import com.inatel.quotationmanagement.quotationmanagement.models.Stock;
import com.inatel.quotationmanagement.quotationmanagement.models.StockQuote;
import com.inatel.quotationmanagement.quotationmanagement.models.forms.StockQuotesForm;
import com.inatel.quotationmanagement.quotationmanagement.repository.StockRepository;
import com.inatel.quotationmanagement.quotationmanagement.services.CreateNewStockQuoteService;
import com.inatel.quotationmanagement.quotationmanagement.validation.QuoteDateValidator;
import com.inatel.quotationmanagement.quotationmanagement.controllers.StockController;
import com.inatel.quotationmanagement.quotationmanagement.repository.StockQuotesRepository;
import com.inatel.quotationmanagement.quotationmanagement.controllers.StockQuotesController;
import com.inatel.quotationmanagement.quotationmanagement.errors.InvalidResourceException;
import com.inatel.quotationmanagement.quotationmanagement.errors.ResourceAlreadyExistsException;
import com.inatel.quotationmanagement.quotationmanagement.errors.ResourceNotFoundException;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class StockQuoteControllerTests {
    @Mock
    StockQuotesController stockQuotesController;

    @Mock
    private StockRepository stockRepository;

    @Mock
    private CreateNewStockQuoteService createNewStockQuoteService;

    @Mock
    private StockQuotesRepository stockQuotesRepository;

    @Mock
    private QuoteDateValidator quoteDateValidator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        stockRepository = Mockito.mock(StockRepository.class);
        stockQuotesRepository = Mockito.mock(StockQuotesRepository.class);
        quoteDateValidator = Mockito.mock(QuoteDateValidator.class);
        createNewStockQuoteService = Mockito.mock(CreateNewStockQuoteService.class);

        stockQuotesController = new StockQuotesController(stockRepository, stockQuotesRepository, quoteDateValidator);
    }

    @Test
    void shouldFetchAllStockQuotesSuccessfullyTest() {
        List<StockQuote> listOfStockQuotes = stockQuotes();
        when(stockQuotesRepository.findAll()).thenReturn(listOfStockQuotes);

        List<StockQuote> result = stockQuotesController.list().getBody();

        assertTrue(result.size() == 2);
        assertEquals(result.get(1).getStockId(), "vale5");
    }

    @Test
    void shouldFetchExistingStockQuoteWithStockIdTest() {
        List<StockQuote> listOfStockQuotes = stockQuotes();
        when(stockQuotesRepository.findByStockId(listOfStockQuotes.get(1).getStockId())).thenReturn(Optional.of(listOfStockQuotes.get(1)));

        StockQuote result = stockQuotesController.findByStockId(listOfStockQuotes.get(1).getStockId()).getBody();
        
        assertEquals(result.getStockId(), "vale5");
        assertTrue(result.getQuotes().containsKey("2020-10-22") && result.getQuotes().get("2020-10-22").equals(new BigDecimal(20.0)));
    }

    @Test
    void shouldThrowAnExceptionIfStockDoesNotExistWhenTryingToCreateStockQuoteTest() {
        StockQuotesForm stockQuotesForm = stockQuotesForm();
        when(stockRepository.getStock("petr4")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> stockQuotesController.createOrUpdate(stockQuotesForm));
    }


    @Test
    void shouldThrowAnExceptionIfSomeOfTheDatesOnFormAreOnAnInvalidFormatTest() {
        StockQuotesForm stockQuotesForm = stockQuotesForm();
        stockQuotesForm.addToQuotes("2019-09", new BigDecimal(10.0));
        when(stockRepository.getStock("petr4")).thenReturn(new Stock("petr4", "Petrobrás, BR"));
        when(stockQuotesRepository.findByStockId("petr4")).thenReturn(Optional.ofNullable(null));

        assertThrows(InvalidResourceException.class, () -> stockQuotesController.createOrUpdate(stockQuotesForm));
    }

    @Test
    void shouldThrowAnExceptionIfSomeOfTheDatesOnTheFormAlreadyExistsOnItsRelatedStockQuoteTest() {
        List<StockQuote> listOfStockQuotes = stockQuotes();
        StockQuotesForm stockQuotesForm = stockQuotesForm();
        stockQuotesForm.addToQuotes("2020-10-10", new BigDecimal(10.0));
        when(stockRepository.getStock("petr4")).thenReturn(new Stock("petr4", "Petrobrás, BR"));
        when(stockQuotesRepository.findByStockId("petr4")).thenReturn(Optional.of(listOfStockQuotes.get(0)));
        when(quoteDateValidator.isValid(Mockito.any())).thenReturn(true);

        assertThrows(InvalidResourceException.class, () -> stockQuotesController.createOrUpdate(stockQuotesForm));
    }

    @Test
    void shouldSuccessfullyCreateAStockQuoteTest() {
        StockQuotesForm stockQuotesForm = stockQuotesForm();
        when(stockRepository.getStock("petr4")).thenReturn(new Stock("petr4", "Petrobrás, BR"));
        when(stockQuotesRepository.findByStockId("petr4")).thenReturn(Optional.ofNullable(null));
        when(quoteDateValidator.isValid(Mockito.any())).thenReturn(true);
        when(stockQuotesRepository.save(Mockito.any())).thenReturn(new StockQuote(stockQuotesForm.getStockId(), stockQuotesForm.getQuotes()));

        StockQuote result = stockQuotesController.createOrUpdate(stockQuotesForm).getBody();

        assertEquals(result.getStockId(), "petr4");
        assertTrue(result.getQuotes().containsKey("2019-10-15"));
    }

    private List<StockQuote> stockQuotes() {
        Stock firstStock = new Stock("petr4", "Petrobrás, Brasil");
        Map<String, BigDecimal> quotes = new HashMap<String, BigDecimal>();
        quotes.put("2020-10-10", new BigDecimal(10.0));
        quotes.put("2021-09-12", new BigDecimal(25.0));

        Stock secondStock = new Stock("vale5", "Vale, Brasil");
        Map<String, BigDecimal> secondQuotes = new HashMap<String, BigDecimal>();
        secondQuotes.put("2018-11-21", new BigDecimal(12.0));
        secondQuotes.put("2020-10-22", new BigDecimal(20.0));

        StockQuote stockQuotes = new StockQuote(firstStock.getId(), quotes);
        StockQuote secondStockQuotes = new StockQuote(secondStock.getId(), secondQuotes);

        List<StockQuote> stockQuotesList = new ArrayList<StockQuote>();

        stockQuotesList.add(stockQuotes);
        stockQuotesList.add(secondStockQuotes);

        return stockQuotesList;
    }

    private StockQuotesForm stockQuotesForm() {
        Map<String, BigDecimal> quotes = new HashMap<String, BigDecimal>();
        quotes.put("2019-10-15", new BigDecimal(25.0));
        
        StockQuotesForm stockQuoteForm = new StockQuotesForm("petr4", quotes);

        return stockQuoteForm;
    }
}
