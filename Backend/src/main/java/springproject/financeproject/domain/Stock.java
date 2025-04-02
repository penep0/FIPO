package springproject.financeproject.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
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
public class Stock {

    @Id
    private String isinCd;      // ISIN 코드
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
    private double vs;          // 대비
    private double fltRt;       // 등락률
    private long lstgStCnt;     // 상장주식수
    private long mrktTotAmt;    // 시가총액

    @Builder
    public Stock(String isinCd, String srtnCd, LocalDate basDt, String itmsNm, String mrktCtg,long mkp, long hipr, long lopr, long trqu, long trPrc, double clpr, double vs, double fltRt, long lstgStCnt, long mrktTotAmt) {
        this.isinCd = isinCd;
        this.srtnCd = srtnCd;
        this.basDt = basDt;
        this.itmsNm = itmsNm;
        this.mrktCtg = mrktCtg;
        this.mkp = mkp;
        this.hipr = hipr;
        this.lopr = lopr;
        this.trqu = trqu;
        this.trPrc = trPrc;
        this.clpr = clpr;
        this.vs = vs;
        this.fltRt = fltRt;
        this.lstgStCnt = lstgStCnt;
        this.mrktTotAmt = mrktTotAmt;
    }
}
