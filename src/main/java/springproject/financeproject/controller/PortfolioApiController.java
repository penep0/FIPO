package springproject.financeproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import springproject.financeproject.domain.Portfolio;
import springproject.financeproject.domain.PortfolioStock;
import springproject.financeproject.domain.Stock;
import springproject.financeproject.domain.User;
import springproject.financeproject.dto.PortfolioRequest;
import springproject.financeproject.dto.PortfolioStockResponse;
import springproject.financeproject.repository.PortfolioRepository;
import springproject.financeproject.service.PortfolioService;
import springproject.financeproject.service.StockService;
import springproject.financeproject.service.UserService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PortfolioController {
    private final PortfolioService portfolioService;
    private final StockService stockService;
    private final UserService userService;
    private final PortfolioRepository portfolioRepository;

    //포트폴리오 생성
    @PostMapping("/create-portfolio")
    public String createPortfolio(@RequestBody PortfolioRequest portfolioRequest) {
        List<PortfolioStock>  portfolioStocks = new ArrayList<>();
        portfolioStocks = portfolioService.savePortfolioStocks(portfolioRequest.getStockNum(), portfolioRequest.getStocks());
        portfolioService.savePortfolio(portfolioRequest.getUser(), portfolioStocks);
        return "Portfolio created";
    }

    //포트폴리오 수정
    @PostMapping("/sell-or-purchse-stock")
    public String sellOrPurchseStock(@RequestBody PortfolioRequest portfolioRequest) {

        return "Portfolio updated";
    }

    //포트폴리오 삭제
    @DeleteMapping("/delete-portfolio")
    public String deletePortfolio(@RequestParam Long portfolioId) {
        portfolioService.deletePortfolio(portfolioId);
        return "Portfolio deleted";
    }
}
