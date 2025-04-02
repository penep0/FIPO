package springproject.financeproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springproject.financeproject.service.StockService;

@RestController
@RequiredArgsConstructor
public class StockApiController {

    @Autowired
    private final StockService stockService;

    @GetMapping("/fetch-and-save-stock-data")
    public String fetchAndSaveStockData(@RequestParam String endpoint) {
        stockService.saveStockData(endpoint);
            return "Stock data fetched and saved successfully.";
    }

    @GetMapping("/save-kospi-stocks")
    public String saveAllStocks() {
        stockService.saveKOSPIStockData();
        return "All stock data fetched and saved successfully.";
    }

    @DeleteMapping("/delete-all-stocks")
    public String deleteAllStocks() {
        stockService.deleteAllStockData();
        return "All stock data deleted successfully.";
    }
}
