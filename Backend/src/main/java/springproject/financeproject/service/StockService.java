package springproject.financeproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import springproject.financeproject.dto.StockApiResponse;
import springproject.financeproject.dto.StockDto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class StockService {

    private final RestClient restClient;

    @Value("${publicdata.api.key}")
    private String apiKey;

    @Value("${publicdata.api.url}")
    private String apiUrl;


    @Autowired
    public StockService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    public List<StockDto> loadStockData(String endpoint) {
        // 서비스 키와 JSON 응답 타입을 포함한 전체 URI 생성
        String fullUri = String.format("%s%s?serviceKey=%s&resultType=json", apiUrl, endpoint, apiKey);

        ResponseEntity<StockApiResponse> response = restClient.method(HttpMethod.GET)
                .uri(fullUri)
                .retrieve()
                .toEntity(StockApiResponse.class);
        List<StockDto> stocks = new ArrayList<>();
        StockApiResponse stockApiResponse = response.getBody();
        if (stockApiResponse != null && stockApiResponse.getResponse().getBody() != null) {
            stockApiResponse.getResponse().getBody().getItems().getItem().forEach(item -> {
                StockDto stock = StockDto.builder()
                        .isinCd(item.getIsinCd())
                        .srtnCd(item.getSrtnCd())
                        .basDt(LocalDate.parse(item.getBasDt(), DateTimeFormatter.BASIC_ISO_DATE))
                        .itmsNm(item.getItmsNm())
                        .mrktCtg(item.getMrktCtg())
                        .mkp(Long.parseLong(item.getMkp()))
                        .hipr(Long.parseLong(item.getHipr()))
                        .lopr(Long.parseLong(item.getLopr()))
                        .trqu(Long.parseLong(item.getTrqu()))
                        .trPrc(Long.parseLong(item.getTrPrc()))
                        .clpr(Double.parseDouble(item.getClpr()))
                        .vs(Double.parseDouble(item.getVs()))
                        .fltRt(Double.parseDouble(item.getFltRt()))
                        .lstgStCnt(Long.parseLong(item.getLstgStCnt()))
                        .mrktTotAmt(Long.parseLong(item.getMrktTotAmt()))
                        .build();

                stocks.add(stock);
            });
        }
        return stocks;
    }

    public List<StockDto> loadKOSPIStockData(int page) {
        int pageNo = 1;
        boolean hasNextPage = true;
        int maxPages = page; // 최대 페이지 제한 설정 (예: 100페이지)
        List<StockDto> stocks = new ArrayList<>();
        while (hasNextPage && pageNo <= maxPages) {
            String fullUri = String.format("%s/getStockPriceInfo?serviceKey=%s&resultType=json&pageNo=%d&mrktCls=KOSPI", apiUrl, apiKey, pageNo);

            ResponseEntity<StockApiResponse> response = restClient.method(HttpMethod.GET)
                    .uri(fullUri)
                    .retrieve()
                    .toEntity(StockApiResponse.class);

            StockApiResponse stockApiResponse = response.getBody();
            if (stockApiResponse != null && stockApiResponse.getResponse().getBody() != null) {
                if (stockApiResponse.getResponse().getBody().getItems().getItem().isEmpty()) {
                    hasNextPage = false; // 데이터가 없으면 루프 종료
                } else {
                    stockApiResponse.getResponse().getBody().getItems().getItem().forEach(item -> {
                        StockDto stock = StockDto.builder()
                                .isinCd(item.getIsinCd())
                                .srtnCd(item.getSrtnCd())
                                .basDt(LocalDate.parse(item.getBasDt(), DateTimeFormatter.BASIC_ISO_DATE))
                                .itmsNm(item.getItmsNm())
                                .mrktCtg(item.getMrktCtg())
                                .mkp(Long.parseLong(item.getMkp()))
                                .hipr(Long.parseLong(item.getHipr()))
                                .lopr(Long.parseLong(item.getLopr()))
                                .trqu(Long.parseLong(item.getTrqu()))
                                .trPrc(Long.parseLong(item.getTrPrc()))
                                .clpr(Double.parseDouble(item.getClpr()))
                                .vs(Double.parseDouble(item.getVs()))
                                .fltRt(Double.parseDouble(item.getFltRt()))
                                .lstgStCnt(Long.parseLong(item.getLstgStCnt()))
                                .mrktTotAmt(Long.parseLong(item.getMrktTotAmt()))
                                .build();

                        stocks.add(stock);
                    });
                    pageNo++; // 다음 페이지로 이동
                }
            } else {
                hasNextPage = false; // 응답이 없으면 루프 종료
            }
        }
        return stocks;
    }

    public List<StockDto> loadKOSDAQStockData() {
        int pageNo = 1;
        boolean hasNextPage = true;
        int maxPages = 100; // 최대 페이지 제한 설정 (예: 100페이지)
        List<StockDto> stocks = new ArrayList<>();
        while (hasNextPage && pageNo <= maxPages) {
            String fullUri = String.format("%s/getStockPriceInfo?serviceKey=%s&resultType=json&pageNo=%d&mrktCls=KOSAQ", apiUrl, apiKey, pageNo);

            ResponseEntity<StockApiResponse> response = restClient.method(HttpMethod.GET)
                    .uri(fullUri)
                    .retrieve()
                    .toEntity(StockApiResponse.class);

            StockApiResponse stockApiResponse = response.getBody();
            if (stockApiResponse != null && stockApiResponse.getResponse().getBody() != null) {
                if (stockApiResponse.getResponse().getBody().getItems().getItem().isEmpty()) {
                    hasNextPage = false; // 데이터가 없으면 루프 종료
                } else {
                    stockApiResponse.getResponse().getBody().getItems().getItem().forEach(item -> {
                        StockDto stock = StockDto.builder()
                                .isinCd(item.getIsinCd())
                                .srtnCd(item.getSrtnCd())
                                .basDt(LocalDate.parse(item.getBasDt(), DateTimeFormatter.BASIC_ISO_DATE))
                                .itmsNm(item.getItmsNm())
                                .mrktCtg(item.getMrktCtg())
                                .mkp(Long.parseLong(item.getMkp()))
                                .hipr(Long.parseLong(item.getHipr()))
                                .lopr(Long.parseLong(item.getLopr()))
                                .trqu(Long.parseLong(item.getTrqu()))
                                .trPrc(Long.parseLong(item.getTrPrc()))
                                .clpr(Double.parseDouble(item.getClpr()))
                                .vs(Double.parseDouble(item.getVs()))
                                .fltRt(Double.parseDouble(item.getFltRt()))
                                .lstgStCnt(Long.parseLong(item.getLstgStCnt()))
                                .mrktTotAmt(Long.parseLong(item.getMrktTotAmt()))
                                .build();

                        stocks.add(stock);

                    });
                    pageNo++; // 다음 페이지로 이동
                }
            } else {
                hasNextPage = false; // 응답이 없으면 루프 종료
            }
        }
        return stocks;
    }
}
