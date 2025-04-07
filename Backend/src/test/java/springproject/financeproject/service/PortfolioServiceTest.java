package springproject.financeproject.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import springproject.financeproject.domain.Portfolio;
import springproject.financeproject.domain.User;
import springproject.financeproject.dto.StockDto;
import springproject.financeproject.repository.PortfolioRepository;
import springproject.financeproject.repository.UserRepository;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class PortfolioServiceTest {

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Test
    void 포트폴리오_생성_테스트() {
        // given
        User user = new User();
        user.setId(1L);
        user.setUserName("테스터");
        user.setPassword("1234");
        user.setMoney(1_000_000L);
        userRepository.save(user);

        // when
        Portfolio portfolio = portfolioService.createPortfolio(user, "나의 포트폴리오");

        // then
        assertThat(portfolio.getPortfolioName()).isEqualTo("나의 포트폴리오");
        assertThat(portfolio.getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    void 주식_매수_테스트() {
        // given
        User user = new User();
        user.setId(2L);
        user.setUserName("매수유저");
        user.setPassword("1234");
        user.setMoney(1_000_000L);
        userRepository.save(user);

        Portfolio portfolio = portfolioService.createPortfolio(user, "매수 포트폴리오");

        StockDto mockStock = StockDto.builder()
                .isinCd("TEST123")
                .itmsNm("테스트종목")
                .mrktCtg("KOSPI")
                .mkp(1000L)
                .basDt(String.valueOf(LocalDate.now()))
                .build();

        // when
        portfolioService.addStockInPortfolio(portfolio, mockStock, 5);

        // then
        assertThat(portfolio.getStocks()).hasSize(1);
        assertThat(portfolio.getStocks().get(0).getStockNum()).isEqualTo(5);
        assertThat(portfolio.getTotalCash()).isEqualTo(5000L);
        assertThat(user.getMoney()).isEqualTo(995000L);
    }
}