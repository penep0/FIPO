package springproject.financeproject.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockResponse {

    private String isinCd;      // ISIN 코드
    private String srtnCd;      // 단축코드
    private LocalDate basDt;    // 기준일자
    private String itmsNm;      // 종목명
    private String mrktCtg;     // 시장 구분
    private long mkp;           // 시가
    private long hipr;          // 고가
    private long lopr;          // 저가
    private long trqu;          // 거래량
    private long trPrc;         // 거래대금
    private double clpr;        // 종가
    private double vs;          // 대비
    private double fltRt;       // 등락률
    private long lstgStCnt;     // 상장주식수
    private long mrktTotAmt;    // 시가총액

    // Static method to convert from entity to DTO
    public static StockResponse fromEntity(Stock stock) {
        return StockResponse.builder()
                .isinCd(stock.getIsinCd())
                .srtnCd(stock.getSrtnCd())
                .basDt(stock.getBasDt())
                .itmsNm(stock.getItmsNm())
                .mrktCtg(stock.getMrktCtg())
                .mkp(stock.getMkp())
                .hipr(stock.getHipr())
                .lopr(stock.getLopr())
                .trqu(stock.getTrqu())
                .trPrc(stock.getTrPrc())
                .clpr(stock.getClpr())
                .vs(stock.getVs())
                .fltRt(stock.getFltRt())
                .lstgStCnt(stock.getLstgStCnt())
                .mrktTotAmt(stock.getMrktTotAmt())
                .build();
    }
}
