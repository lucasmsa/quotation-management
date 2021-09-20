package com.inatel.quotationmanagement.quotationmanagement.stock;

import java.util.List;
import org.mockito.Mock;
import org.mockito.Mockito;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import com.inatel.quotationmanagement.quotationmanagement.models.Stock;
import com.inatel.quotationmanagement.quotationmanagement.repository.StockRepository;
import com.inatel.quotationmanagement.quotationmanagement.controllers.StockController;
import com.inatel.quotationmanagement.quotationmanagement.errors.ResourceAlreadyExistsException;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class StockControllerTests {
	@Mock
	StockController stockController;

	@Mock
	private StockRepository stockRepository;

	@BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        stockRepository = Mockito.mock(StockRepository.class);
				stockController = new StockController(stockRepository);
    }

		@Test
		void shouldFetchAllStockSuccessfullyTest() {
			List<Stock> listOfStocks = stocks();
			when(stockRepository.getAllStocks()).thenReturn(listOfStocks);

			List<Stock> result = stockController.list().getBody();

			assertTrue(result.size() == 2);
			assertEquals(result.get(0).getId(), "vale5");
		}

		@Test
		void shouldAddStockSuccessfullyTest() {
			Stock stock = new Stock("AMZO34", "Amazon, USA");

			Stock result = stockController.create(stock).getBody();

			assertTrue(result.getId().equals("AMZO34"));
			assertEquals(result.getDescription(), "Amazon, USA");
		}

		@Test
		void shouldThrowAnExceptionIfTryingToAddAStockWithAnIdThatAlreadyExistsTest() {
			List<Stock> listOfStocks = stocks();
			Stock stock = new Stock("vale5", "Vale, BR");
			when(stockRepository.getStock("vale5")).thenReturn(listOfStocks.get(0));

			assertThrows(ResourceAlreadyExistsException.class, () -> stockController.create(stock));
		}

		private List<Stock> stocks() {
			List<Stock> listOfStocks = new ArrayList<>();
			Stock firstStock = new Stock("vale5", "Vale, Brasil");
			Stock secondStock = new Stock("GOOGL34", "Google, Brasil");

			listOfStocks.add(firstStock);
			listOfStocks.add(secondStock);

			return listOfStocks;
		}
}
