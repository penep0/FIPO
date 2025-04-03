package springproject.financeproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springproject.financeproject.dto.StockApiResponse;
import springproject.financeproject.dto.StockDto;
import springproject.financeproject.service.StockService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class StockViewController {

    private final StockService stockService;

    @GetMapping("/stocks")
    public String getKOSPIStockList(Model model) {
        List<StockDto> stocks = stockService.loadKOSPIStockData(3);
        model.addAttribute("stocks", stocks);
        model.addAttribute("stockCount", stocks.size()); // 총 개수 추가
        return "stockList"; // stockList.html을 반환
    }
}
