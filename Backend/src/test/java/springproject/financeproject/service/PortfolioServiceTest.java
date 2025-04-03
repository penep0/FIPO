package springproject.financeproject.service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import springproject.financeproject.domain.*;
import springproject.financeproject.dto.StockDto;
import springproject.financeproject.repository.PortfolioRepository;
import springproject.financeproject.repository.PortfolioStockRepository;
import springproject.financeproject.service.PortfolioService;
import springproject.financeproject.service.StockService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PortfolioServiceTest {

    @Mock
    private PortfolioRepository portfolioRepository;

    @Mock
    private PortfolioStockRepository portfolioStockRepository;

    @Mock
    private StockService stockService;

    @InjectMocks
    private PortfolioService portfolioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSellStockInPortfolio_Success() {
        Portfolio portfolio = new Portfolio();
        PortfolioStock stock = new PortfolioStock();
        stock.setItmsNm("삼성전자");
        stock.setIsinCd("123456");
        stock.setStockNum(10);
        stock.setCash(100000L);
        portfolio.setStocks(new ArrayList<>(List.of(stock)));

        StockDto stockDto = StockDto.builder()
                .itmsNm("삼성전자")
                .isinCd("123456")
                .build();

        StockDto marketPrice = StockDto.builder().isinCd("123456").mkp(12000L).build();
        when(stockService.loadStockDataByIsinDc("123456")).thenReturn(marketPrice);

        portfolioService.sellStockInPortfolio(portfolio, stockDto, 5, 60000L);

        assertEquals(5, stock.getStockNum());
        assertEquals(160000L, stock.getCash());
    }

    @Test
    void testSellStockInPortfolio_NotOwned() {
        Portfolio portfolio = new Portfolio();
        portfolio.setStocks(new ArrayList<>());

        StockDto stockDto = StockDto.builder()
                .itmsNm("없는종목")
                .isinCd("000000")
                .build();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            portfolioService.sellStockInPortfolio(portfolio, stockDto, 1, 1000L);
        });

        assertTrue(exception.getMessage().contains("해당 주식을 보유하고 있지 않습니다"));
    }

    @Test
    void testSellStockInPortfolio_TooMany() {
        PortfolioStock stock = new PortfolioStock();
        stock.setItmsNm("삼성전자");
        stock.setIsinCd("123456");
        stock.setStockNum(3);
        stock.setCash(50000L);

        Portfolio portfolio = new Portfolio();
        portfolio.setStocks(new ArrayList<>(List.of(stock)));

        StockDto stockDto = StockDto.builder()
                .itmsNm("삼성전자")
                .isinCd("123456")
                .build();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            portfolioService.sellStockInPortfolio(portfolio, stockDto, 5, 100000L);
        });

        assertTrue(exception.getMessage().contains("보유 수량보다 많은 수량을 판매할 수 없습니다."));
    }
}