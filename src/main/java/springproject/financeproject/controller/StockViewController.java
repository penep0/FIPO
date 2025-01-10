package springproject.financeproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springproject.financeproject.domain.Stock;
import springproject.financeproject.dto.StockResponse;
import springproject.financeproject.repository.StockRepository;
import springproject.financeproject.service.StockService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class StockViewController {

    private final StockRepository stockRepository;

    @GetMapping("/stocks")
    public String getStockList(Model model) {
        List<Stock> stocks = stockRepository.findAll();
        List<StockResponse> stockResponses = stocks.stream()
                .map(StockResponse::fromEntity) // 각 Stock 엔티티를 StockResponse로 변환
                .collect(Collectors.toList());  // 변환된 DTO 리스트를 생성
        model.addAttribute("stocks", stockResponses);
        model.addAttribute("stockCount", stockResponses.size()); // 총 개수 추가
        return "stockList"; // stockList.html을 반환
    }
}
