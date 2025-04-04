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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
                        .basDt(item.getBasDt())
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
                                .basDt(item.getBasDt())
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

    public List<StockDto> loadKOSPIStockDataPage(int page) {
        int pageNo = page;
        List<StockDto> stocks = new ArrayList<>();
        String fullUri = String.format("%s/getStockPriceInfo?serviceKey=%s&resultType=json&pageNo=%d&mrktCls=KOSPI", apiUrl, apiKey, pageNo);

        ResponseEntity<StockApiResponse> response = restClient.method(HttpMethod.GET)
                .uri(fullUri)
                .retrieve()
                .toEntity(StockApiResponse.class);

        StockApiResponse stockApiResponse = response.getBody();
        if (stockApiResponse != null && stockApiResponse.getResponse().getBody() != null) {
            if (stockApiResponse.getResponse().getBody().getItems().getItem().isEmpty()) {
                return stocks;
            } else {
                stockApiResponse.getResponse().getBody().getItems().getItem().forEach(item -> {
                    StockDto stock = StockDto.builder()
                            .isinCd(item.getIsinCd())
                            .srtnCd(item.getSrtnCd())
                            .basDt(item.getBasDt())
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
                                .basDt(item.getBasDt())
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

    //isinCd로 주식 찾아서 반
    public StockDto loadStockDataByIsinDc(String isinCd) {
        String fullUri = String.format("%s/getStockPriceInfo?serviceKey=%s&resultType=json&isinCd=%s", apiUrl, apiKey, isinCd);

        ResponseEntity<StockApiResponse> response = restClient.method(HttpMethod.GET)
                .uri(fullUri)
                .retrieve()
                .toEntity(StockApiResponse.class);
        // null-safe하게 getItem()까지 접근
        List<StockApiResponse.Response.Body.Items.Item> items =
                Optional.ofNullable(response.getBody())
                        .map(StockApiResponse::getResponse)
                        .map(StockApiResponse.Response::getBody)
                        .map(StockApiResponse.Response.Body::getItems)
                        .map(StockApiResponse.Response.Body.Items::getItem)
                        .orElse(Collections.emptyList());
        // 첫 번째 데이터만 사용해서 StockDto로 변환
        StockApiResponse.Response.Body.Items.Item item = items.get(0);

        // 비어 있다면 예외 발생
        if (items.isEmpty()) {
            throw new RuntimeException("해당 ISIN 코드에 대한 주식 데이터를 찾을 수 없습니다: " + isinCd);
        }

        return StockDto.builder()
                .isinCd(isinCd)
                .srtnCd(item.getIsinCd())
                .basDt(item.getBasDt())
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
    }
}