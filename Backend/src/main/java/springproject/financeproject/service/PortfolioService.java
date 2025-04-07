package springproject.financeproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
@Transactional
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
    public PortfolioStock createPortfolioStock(StockDto stockDto, int stockNum) {
        long cash = stockDto.getMkp() * stockNum;
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
        long earningMoney = 0;
        double proceeds;

        for (PortfolioStock portfolioStock : portfolio.getStocks()) {
            totalCash += portfolioStock.getCash();
            earningMoney += portfolioStock.getEarningMoney();
        }

        if (totalCash == 0) {
            proceeds = 0.0;
        } else {
            proceeds = (double) earningMoney / totalCash * 100;
        }

        portfolio.setEarningRate((long) proceeds); // 수익률은 정수로 저장
        portfolio.setProceeds(proceeds); // 수익금은 double로 저장
    }

    //주식별 수익률 계산
    public void updateStockProceedAndEarningMoney (PortfolioStock portfolioStock) {
        StockDto stockDto = stockService.loadStockDataByIsinCd(portfolioStock.getIsinCd());
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
    public void addStockInPortfolio(Portfolio portfolio, StockDto stockDto, int stockNum) {
        //포트폴리오 안에 같은 이름의 주식이 있나 확인.
        Optional<PortfolioStock> existingStock = portfolio.getStocks().stream()
                .filter(ps -> ps.getItmsNm().equals(stockDto.getItmsNm()))
                .findFirst();

        long cash = stockDto.getMkp() * stockNum;

        if (existingStock.isPresent()) {
            PortfolioStock target = existingStock.get();
            target.setStockNum(target.getStockNum() + stockNum);
            portfolio.getUser().setMoney(portfolio.getUser().getMoney() - cash);
            target.setCash(target.getCash() + cash);
            portfolio.setTotalCash(portfolio.getTotalCash() + cash);
            updateStockProceedAndEarningMoney(existingStock.get());
            updatePortfolioProceed(portfolio);
        }else {
            PortfolioStock portfolioStock = createPortfolioStock(stockDto, stockNum);
            portfolioStock.setPortfolio(portfolio);
            portfolio.getStocks().add(portfolioStock);
            portfolioStockRepository.save(portfolioStock);
            portfolio.getUser().setMoney(portfolio.getUser().getMoney() - cash);
            portfolio.setTotalCash(portfolio.getTotalCash() + cash);
            updatePortfolioProceed(portfolio);
        }
    }

    //주식 판매
    public void sellStockInPortfolio(Portfolio portfolio, StockDto stockDto, int stockNum) {
        Optional<PortfolioStock> existingStock = portfolio.getStocks().stream()
                .filter(ps -> ps.getItmsNm().equals(stockDto.getItmsNm()))
                .findFirst();
        long cash = stockDto.getMkp() * stockNum;
        if (existingStock.isPresent()) {
            PortfolioStock target = existingStock.get();

            if (target.getStockNum() < stockNum) {
                throw new RuntimeException("보유 수량보다 많은 수량을 판매할 수 없습니다.");
            }

            portfolio.getUser().setMoney(portfolio.getUser().getMoney() + cash);
            portfolio.setTotalCash(portfolio.getTotalCash() - cash);
            target.setStockNum(target.getStockNum() - stockNum);
            target.setCash(target.getCash() - cash);

            // 수익률 및 수익금 갱신
            updateStockProceedAndEarningMoney(target);
            updatePortfolioProceed(portfolio);

            // 보유 수량이 0이면 제거
            if (target.getStockNum() == 0) {
                portfolio.getStocks().remove(target);
                portfolioStockRepository.delete(target);
            }

        } else {
            throw new RuntimeException("해당 주식을 보유하고 있지 않습니다: " + stockDto.getItmsNm());
        }
    }
}
