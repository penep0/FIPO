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

    @Builder
    public PortfolioStock(int stockNum, long cash, LocalDate basDt, String itmsNm, String mrktCtg, long mkp) {
        this.stockNum = stockNum;
        this.cash = cash;
        this.basDt = basDt;
        this.itmsNm = itmsNm;
        this.mrktCtg = mrktCtg;
        this.mkp = mkp;
        this.proceeds = 0;
    }
}
