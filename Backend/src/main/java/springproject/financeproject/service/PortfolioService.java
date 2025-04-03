package springproject.financeproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springproject.financeproject.domain.Portfolio;
import springproject.financeproject.domain.PortfolioStock;
import springproject.financeproject.domain.User;
import springproject.financeproject.dto.StockDto;
import springproject.financeproject.repository.PortfolioRepository;
import springproject.financeproject.repository.PortfolioStockRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PortfolioService {
    private final PortfolioRepository portfolioRepository;
    private final PortfolioStockRepository portfolioStockRepository;
    private final StockService stockService;

    @Autowired
    public PortfolioService(PortfolioRepository portfolioRepository, PortfolioStockRepository portfolioStockRepository, StockService stockService) {
        this.portfolioRepository = portfolioRepository;
        this.portfolioStockRepository = portfolioStockRepository;
        this.stockService = stockService;
    }

    //포트폴리오에 추가할 주식 생성 (구매 시점, 구매시 시가 등)
    public PortfolioStock createPortfolioStock(StockDto stockDto, int stockNum, long cash) {
        PortfolioStock portfolioStock = PortfolioStock.builder()
                .isinCd(stockDto.getIsinCd())
                .stockNum(stockNum)
                .cash(cash)
                .basDt(stockDto.getBasDt())
                .itmsNm(stockDto.getItmsNm())
                .mrktCtg(stockDto.getMrktCtg())
                .mkp(stockDto.getMkp())
                .build();

        return portfolioStockRepository.save(portfolioStock);
    }

    //포트폴리오 수익률 계산
    public void updatePortfolioProceed(Portfolio portfolio) {
        long totalCash = 0;
        long earningRate = 0;
        double proceeds = 0;
        for (PortfolioStock portfolioStock : portfolio.getStocks()) {
            totalCash += portfolioStock.getCash();
            earningRate += portfolioStock.getEarningMoney();
        }
        proceeds = earningRate/totalCash * 100;
        portfolio.setEarningRate(earningRate);
        portfolio.setProceeds(proceeds);
    }

    //주식별 수익률 계산
    public void updateStockProceedAndEarningMoney (PortfolioStock portfolioStock) {
        StockDto stockDto = stockService.loadStockDataByIsinDc(portfolioStock.getIsinCd());
        double proceeds = (double) (stockDto.getMkp() - portfolioStock.getMkp()) / stockDto.getMkp();
        long money = (stockDto.getMkp() - portfolioStock.getMkp()) * portfolioStock.getStockNum();
        portfolioStock.setProceeds(proceeds);
        portfolioStock.setEarningMoney(money);
    }

    //포트폴리오 생성
    public Portfolio createPortfolio(User user, String portfolioName) {
        Portfolio portfolio = Portfolio.builder()
                .user(user)
                .portfolioName(portfolioName)
                .build();

        return portfolioRepository.save(portfolio);
    }

    //주식 구매
    public void addStockInPortfolio(Portfolio portfolio, StockDto stockDto, int stockNum, long cash) {
        //포트폴리오 안에 같은 이름의 주식이 있나 확인.
        Optional<PortfolioStock> existingStock = portfolio.getStocks().stream()
                .filter(ps -> ps.getItmsNm().equals(stockDto.getItmsNm()))
                .findFirst();

        if (existingStock.isPresent()) {
            PortfolioStock target = existingStock.get();
            target.setStockNum(target.getStockNum() + stockNum);
            target.setCash(target.getCash() + cash);
        }else {
            PortfolioStock portfolioStock = createPortfolioStock(stockDto, stockNum, cash);
            portfolio.getStocks().add(portfolioStock);
        }
    }
}
