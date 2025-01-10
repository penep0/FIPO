package springproject.financeproject.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class StockHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String srtnCd;      // 단축코드
    private LocalDate basDt;       // 기준일자
    private String itmsNm;      // 종목명
    private String mrktCtg;     // 시장 구분
    private long mkp;        // 시가
    private long hipr;       // 고가
    private long lopr;       // 저가
    private long trqu;       // 거래량
    private long trPrc;      // 거래대금
    private double clpr;        // 종가

    @OneToOne
    @JoinColumn(name = "isinCd")
    private Stock stock;

    @Builder
    public StockHistory(Stock stock) {
        this.srtnCd = stock.getSrtnCd();
        this.basDt = stock.getBasDt();
        this.itmsNm = stock.getItmsNm();
        this.mrktCtg = stock.getMrktCtg();
        this.mkp = stock.getMkp();
        this.hipr = stock.getHipr();
        this.lopr = stock.getLopr();
        this.trqu = stock.getTrqu();
        this.trPrc = stock.getTrPrc();
        this.clpr = stock.getClpr();
    }
}
