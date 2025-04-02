package springproject.financeproject.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import springproject.financeproject.domain.Stock;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockDto {

    private String srtnCd;     // 단축코드
    private String isinCd;     // ISIN 코드
    private String itmsNm;     // 종목명
    private String mrktCtg;    // 시장 구분

    @JsonDeserialize(using = NumberDeserializers.LongDeserializer.class)
    private long clpr;       // 종가

    @JsonDeserialize(using = NumberDeserializers.LongDeserializer.class)
    private long vs;         // 대비

    @JsonDeserialize(using = NumberDeserializers.FloatDeserializer.class)
    private float fltRt;      // 등락률

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

    public Stock toEntity(){
        return Stock.builder()
                .srtnCd(srtnCd)
                .isinCd(isinCd)
                .itmsNm(itmsNm)
                .mrktCtg(mrktCtg)
                .clpr(clpr)
                .vs(vs)
                .fltRt(fltRt)
                .mkp(mkp)
                .hipr(hipr)
                .lopr(lopr)
                .trqu(trqu)
                .trPrc(trPrc)
                .lstgStCnt(lstgStCnt)
                .mrktTotAmt(mrktTotAmt)
                .build();
    }
}
