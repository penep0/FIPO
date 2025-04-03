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
    void testCreatePortfolioStock() {
        StockDto stockDto = StockDto.builder()
                .isinCd("123456")
                .itmsNm("삼성전자")
                .basDt("2024.3.12")
                .mrktCtg("KOSPI")
                .mkp(10000L)
                .build();

        PortfolioStock mockSaved = new PortfolioStock();
        mockSaved.setItmsNm("삼성전자");

        when(portfolioStockRepository.save(any())).thenReturn(mockSaved);

        PortfolioStock result = portfolioService.createPortfolioStock(stockDto, 10, 100000);

        assertNotNull(result);
        assertEquals("삼성전자", result.getItmsNm());
    }

    @Test
    void testUpdateStockProceedAndEarningMoney() {
        PortfolioStock portfolioStock = new PortfolioStock();
        portfolioStock.setIsinCd("123456");
        portfolioStock.setMkp(10000L);
        portfolioStock.setStockNum(10);

        StockDto stockDto = StockDto.builder()
                .isinCd("123456")
                .mkp(12000L) // 현재가
                .build();

        when(stockService.loadStockDataByIsinDc("123456")).thenReturn(stockDto);

        portfolioService.updateStockProceedAndEarningMoney(portfolioStock);

        assertEquals(20000L, portfolioStock.getEarningMoney());
        assertTrue(portfolioStock.getProceeds() > 0);
    }

    @Test
    void testAddStockInPortfolio_NewStock() {
        Portfolio portfolio = new Portfolio();
        portfolio.setStocks(new ArrayList<>());

        StockDto stockDto = StockDto.builder()
                .isinCd("ABC")
                .itmsNm("카카오")
                .basDt("2025.2.1")
                .mrktCtg("KOSPI")
                .mkp(90000L)
                .build();

        PortfolioStock mockSaved = new PortfolioStock();
        mockSaved.setItmsNm("카카오");

        when(portfolioStockRepository.save(any())).thenReturn(mockSaved);

        portfolioService.addStockInPortfolio(portfolio, stockDto, 5, 450000);

        assertEquals(1, portfolio.getStocks().size());
        assertEquals("카카오", portfolio.getStocks().get(0).getItmsNm());
    }

    @Test
    void testAddStockInPortfolio_ExistingStock() {
        PortfolioStock existing = new PortfolioStock();
        existing.setItmsNm("삼성전자");
        existing.setStockNum(5);
        existing.setCash(300000);

        Portfolio portfolio = new Portfolio();
        portfolio.setStocks(new ArrayList<>(List.of(existing)));

        StockDto stockDto = StockDto.builder()
                .itmsNm("삼성전자")
                .build();

        portfolioService.addStockInPortfolio(portfolio, stockDto, 3, 180000);

        assertEquals(1, portfolio.getStocks().size());
        assertEquals(8, portfolio.getStocks().get(0).getStockNum());
        assertEquals(480000, portfolio.getStocks().get(0).getCash());
    }
}