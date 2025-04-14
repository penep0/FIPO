package springproject.financeproject.dto.portfolioDto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PortfolioStockResponse {

    private int stockNum;         //보유주식수
    private long cash;
    private String isinCd;      // ISIN 코드
    private LocalDate basDt;       // 기준일자
    private String itmsNm;      // 종목명
    private String mrktCtg;     // 시장 구분
    private long mkp;        // 시가
}
