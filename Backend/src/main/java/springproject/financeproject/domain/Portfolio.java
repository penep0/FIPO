package springproject.financeproject.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
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

    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PortfolioStock> stocks = new ArrayList<>();

    @Builder
    public Portfolio(User user, String portfolioName) {
        this.user = user;
        this.portfolioName = portfolioName;
        this.totalCash = 0L;
        this.earningRate = 0L;
        this.proceeds = 0.0;
    }
}
