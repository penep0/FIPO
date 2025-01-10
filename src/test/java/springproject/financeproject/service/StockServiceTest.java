package springproject.financeproject.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestClient;
import springproject.financeproject.domain.Stock;
import springproject.financeproject.dto.StockApiResponse;
import springproject.financeproject.repository.StockRepository;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class StockServiceTest {

    @InjectMocks
    private StockService stockService;

    @Mock
    private RestClient.Builder restClientBuilder;

    @Mock
    private RestClient restClient;

    @Mock
    private StockRepository stockRepository;

    @Value("${publicdata.api.key}")
    private String apiKey;

    @Value("${publicdata.api.url}")
    private String apiUrl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveStockData() {
        // Mock data for StockApiResponse
        StockApiResponse mockApiResponse = createMockStockApiResponse();
        ResponseEntity<StockApiResponse> responseEntity = ResponseEntity.ok(mockApiResponse);

        // Configure mock restClient behavior
        when(restClient.method(any()))
                .thenReturn((RestClient.RequestBodyUriSpec) responseEntity);

        // Call the method under test
        stockService.saveStockData("/testEndpoint");

        // Verify repository save method was called
        verify(stockRepository, atLeastOnce()).save(any(Stock.class));
    }

    @Test
    void testSaveAllStockData() {
        // Mock data for StockApiResponse with a single page of items
        StockApiResponse mockApiResponse = createMockStockApiResponse();
        ResponseEntity<StockApiResponse> responseEntity = ResponseEntity.ok(mockApiResponse);

        // Configure mock restClient behavior
        when(restClient.method(any()))
                .thenReturn((RestClient.RequestBodyUriSpec) responseEntity);

        // Call the method under test
        stockService.saveAllStockData();

        // Verify repository save method was called
        verify(stockRepository, atLeastOnce()).save(any(Stock.class));
    }

    @Test
    void testDeleteAllStockData() {
        // Call the method under test
        stockService.deleteAllStockData();

        // Verify repository deleteAll method was called
        verify(stockRepository, times(1)).deleteAll();
    }

    @Test
    void testGetAllStocks() {
        // Mock return for findAll()
        when(stockRepository.findAll()).thenReturn(Collections.singletonList(new Stock()));

        // Call the method under test
        List<Stock> stocks = stockService.getAllStocks();

        // Assert result
        assertNotNull(stocks);
        assertFalse(stocks.isEmpty());
        verify(stockRepository, times(1)).findAll();
    }

    private StockApiResponse createMockStockApiResponse() {
        StockApiResponse mockApiResponse = new StockApiResponse();
        // Populate mockApiResponse with necessary test data
        // Customize as needed to match your domain objects
        return mockApiResponse;
    }
}
