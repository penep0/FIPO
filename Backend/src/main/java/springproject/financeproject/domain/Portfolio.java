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

    private String portfolioName;

    private Long totalCash;

    private Long earningRate;

    private double proceeds;

    @ManyToOne
    private User user;

    @OneToMany
    private List<PortfolioStock> stocks;

    @Builder
    public Portfolio(User user, String portfolioName) {
        this.user = user;
        this.portfolioName = portfolioName;
    }
}
