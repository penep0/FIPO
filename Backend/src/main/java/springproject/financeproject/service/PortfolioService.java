package springproject.financeproject.service;

import org.springframework.stereotype.Service;
import springproject.financeproject.domain.Portfolio;
import springproject.financeproject.domain.PortfolioStock;
import springproject.financeproject.domain.User;
import springproject.financeproject.repository.PortfolioRepository;
import springproject.financeproject.repository.PortfolioStockRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class PortfolioService {
    private final PortfolioRepository portfolioRepository;
    private final PortfolioStockRepository portfolioStockRepository;

    public PortfolioService(PortfolioRepository portfolioRepository, PortfolioStockRepository portfolioStockRepository) {
        this.portfolioRepository = portfolioRepository;
        this.portfolioStockRepository = portfolioStockRepository;
    }

    public Portfolio savePortfolio(Long totalCash, User user, List<PortfolioStock> stocks) {
        Portfolio portfolio = Portfolio.builder()
                .totalCash(totalCash)
                .user(user)
                .stocks(stocks)
                .build();

        return portfolioRepository.save(portfolio);
    }

    public void updatePortfolioEarningRate(Portfolio portfolio) {
        List<PortfolioStock> stocks = portfolio.getStocks();
        for(PortfolioStock stock : stocks) {

        }
    }
}
