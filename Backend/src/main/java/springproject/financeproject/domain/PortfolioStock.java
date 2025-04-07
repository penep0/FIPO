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

    private String isinCd;
    private int stockNum;         //보유주식수
    private long cash;            //투자 금액
    private String basDt;       // 기준일자
    private String itmsNm;      // 종목명
    private String mrktCtg;     // 시장 구분
    private long mkp;        // 시가
    private double proceeds; //수익
    private long earningMoney;

    @ManyToOne
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;

    @Builder
    public PortfolioStock(String isinCd, int stockNum, long cash, String basDt, String itmsNm, String mrktCtg, long mkp) {
        this.isinCd = isinCd;
        this.stockNum = stockNum;
        this.cash = cash;
        this.basDt = basDt;
        this.itmsNm = itmsNm;
        this.mrktCtg = mrktCtg;
        this.mkp = mkp;
        this.proceeds = 0;
        this.earningMoney = 0;
    }
}
