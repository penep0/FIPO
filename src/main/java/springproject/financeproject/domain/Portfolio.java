package springproject.financeproject.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long totalCash;

    private Long earningRate;

    private long proceeds;

    @OneToOne
    private User user;

    @OneToMany
    private List<PortfolioStock> stocks;

    @Builder
    public Portfolio(Long totalCash, User user, List<PortfolioStock> stocks) {
        this.totalCash = totalCash;
        this.user = user;
        this.stocks = stocks;
    }

    public Portfolio updateEarningRate() {
        long totalMoney = 0;
        for (PortfolioStock stock : stocks) {
            totalMoney += stock.getProceeds();
        }
        this.earningRate = totalMoney / stocks.size();
        this.proceeds = totalMoney;
        return this;
    }
}
