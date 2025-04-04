package springproject.financeproject.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springproject.financeproject.domain.Portfolio;
import springproject.financeproject.domain.User;
import springproject.financeproject.dto.StockDto;
import springproject.financeproject.jwt.JwtTokenProvider;
import springproject.financeproject.repository.PortfolioRepository;
import springproject.financeproject.repository.UserRepository;
import springproject.financeproject.service.PortfolioService;
import springproject.financeproject.service.StockService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/portfolio")
public class PortfolioApiController {
    private final UserRepository userRepository;
    private final PortfolioRepository portfolioRepository;
    private final PortfolioService portfolioService;
    private final StockService stockService;
    private final JwtTokenProvider jwtTokenProvider;

    // 🔹 포트폴리오 생성
    @PostMapping("/create")
    public ResponseEntity<?> createPortfolio(@RequestParam String name, HttpServletRequest request) {
        String email = extractEmailFromToken(request);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        Portfolio portfolio = portfolioService.createPortfolio(user, name);
        return ResponseEntity.ok(portfolio);
    }

    // 🔹 내 포트폴리오 목록 조회
    @GetMapping("/list")
    public ResponseEntity<?> getMyPortfolios(HttpServletRequest request) {
        String email = extractEmailFromToken(request);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        List<Portfolio> portfolios = user.getPortfolios();
        return ResponseEntity.ok(portfolios);
    }

    // 🔹 주식 구매
    @PostMapping("/{portfolioId}/buy")
    public ResponseEntity<?> buyStock(
            @PathVariable Long portfolioId,
            @RequestParam String isinCd,
            @RequestParam int quantity,
            @RequestParam long cash) {

        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("포트폴리오 없음"));

        StockDto stockDto = stockService.loadStockDataByIsinCd(isinCd);
        portfolioService.addStockInPortfolio(portfolio, stockDto, quantity, cash);

        return ResponseEntity.ok("주식 구매 완료");
    }

    // 🔹 주식 판매
    @PostMapping("/{portfolioId}/sell")
    public ResponseEntity<?> sellStock(
            @PathVariable Long portfolioId,
            @RequestParam String isinCd,
            @RequestParam int quantity,
            @RequestParam long cash) {

        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("포트폴리오 없음"));

        StockDto stockDto = stockService.loadStockDataByIsinCd(isinCd);
        portfolioService.sellStockInPortfolio(portfolio, stockDto, quantity, cash);

        return ResponseEntity.ok("주식 판매 완료");
    }

    // 🔹 토큰에서 사용자 이메일 추출
    private String extractEmailFromToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new RuntimeException("Authorization 헤더 누락");
        }

        String token = header.substring(7);
        return jwtTokenProvider.getUsername(token);
    }
}
