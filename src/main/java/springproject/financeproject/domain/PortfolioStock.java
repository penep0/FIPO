package springproject.financeproject.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PortfolioStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int stockNum;         //보유주식수
    private long cash;
    private LocalDate basDt;       // 기준일자
    private String itmsNm;      // 종목명
    private String mrktCtg;     // 시장 구분
    private long mkp;        // 시가
    private long proceeds; //수익금

    @OneToOne
    @JoinColumn(name = "isinCd")
    private Stock stock;

    @Builder
    public PortfolioStock(int stockNum, Stock stock) {
        this.stockNum = stockNum;
        this.cash = stockNum * stock.getMkp();
        this.basDt = stock.getBasDt();
        this.itmsNm = stock.getItmsNm();
        this.mrktCtg = stock.getMrktCtg();
        this.mkp = stock.getMkp();
        this.stock = stock;
        this.proceeds = 0;
    }
}
