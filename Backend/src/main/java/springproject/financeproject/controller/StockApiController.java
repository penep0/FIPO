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
@RequestMapping("/stock/api")
public class StockApiController {

    private final StockService stockService;

    // API endpoint to get KOSPI stocks
    @GetMapping("/kospi")
    public List<StockDto> getKospiStocks(@RequestParam(defaultValue = "1") int page) {
        return stockService.loadKOSPIStockDataPage(page);
    }
}
