package springproject.financeproject.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockDto {

    private String srtnCd;     // 단축코드
    private String basDt;
    private String isinCd;     // ISIN 코드
    private String itmsNm;     // 종목명
    private String mrktCtg;    // 시장 구분

    @JsonDeserialize(using = NumberDeserializers.LongDeserializer.class)
    private double clpr;       // 종가

    @JsonDeserialize(using = NumberDeserializers.LongDeserializer.class)
    private double vs;         // 대비

    @JsonDeserialize(using = NumberDeserializers.FloatDeserializer.class)
    private double fltRt;      // 등락률

    @JsonDeserialize(using = NumberDeserializers.LongDeserializer.class)
    private long mkp;        // 시가

    @JsonDeserialize(using = NumberDeserializers.LongDeserializer.class)
    private long hipr;       // 고가

    @JsonDeserialize(using = NumberDeserializers.LongDeserializer.class)
    private long lopr;       // 저가

    @JsonDeserialize(using = NumberDeserializers.LongDeserializer.class)
    private long trqu;       // 거래량

    @JsonDeserialize(using = NumberDeserializers.LongDeserializer.class)
    private long trPrc;      // 거래대금

    @JsonDeserialize(using = NumberDeserializers.LongDeserializer.class)
    private long lstgStCnt;  // 상장주식수

    @JsonDeserialize(using = NumberDeserializers.LongDeserializer.class)
    private long mrktTotAmt; // 시가총액
}
