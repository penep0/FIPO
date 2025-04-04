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
@RequestMapping("/stock")
public class StockViewController {

    private final StockService stockService;

    @GetMapping("/kospi")
    public String kospiViewPage() {
        return "stockList"; // templates/stock-list.html 렌더링
    }
}
