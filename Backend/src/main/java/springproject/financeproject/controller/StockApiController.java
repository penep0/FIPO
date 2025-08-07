package springproject.financeproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springproject.financeproject.dto.StockDto;
import springproject.financeproject.service.StockService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stock")
public class StockApiController {

    private final StockService stockService;

    // API endpoint to get KOSPI stocks
    @GetMapping("/kospi")
    public List<StockDto> getKospiStocks(@RequestParam(defaultValue = "1") int page) {
        return stockService.loadKOSPIStockDataPage(page);
    }

    // API endpoint to get KOSDAQ stocks
    @GetMapping("/kosdaq")
    public List<StockDto> getKosdaqStocks(@RequestParam(defaultValue = "1") int page) {
        return stockService.loadKOSDAQStockDataPage(page);
    }

    @GetMapping("/search/itmsNm")
    public StockDto getStockDataByItmsNm(@RequestParam String itmsNm) {
        return stockService.loadStockDataByItmsNm(itmsNm);
    }

    @GetMapping("/search/isinCd")
    public StockDto getStockDataByIsinCd(@RequestParam String isinCd) {
        return stockService.loadStockDataByIsinCd(isinCd);
    }
}
