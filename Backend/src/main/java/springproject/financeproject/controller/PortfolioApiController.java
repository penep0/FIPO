package springproject.financeproject.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import springproject.financeproject.domain.Portfolio;
import springproject.financeproject.domain.PortfolioStock;
import springproject.financeproject.domain.User;
import springproject.financeproject.dto.portfolioDto.PortfolioDto;
import springproject.financeproject.dto.stockDto.StockDto;
import springproject.financeproject.jwt.JwtTokenProvider;
import springproject.financeproject.repository.PortfolioRepository;
import springproject.financeproject.repository.PortfolioStockRepository;
import springproject.financeproject.repository.UserRepository;
import springproject.financeproject.service.PortfolioService;
import springproject.financeproject.service.StockService;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/portfolio")
public class PortfolioApiController {
    private final UserRepository userRepository;
    private final PortfolioRepository portfolioRepository;
    private final PortfolioService portfolioService;
    private final StockService stockService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PortfolioStockRepository portfolioStockRepository;

    // 🔹 포트폴리오 생성
    @PostMapping("/create")
    public ResponseEntity<?> createPortfolio(@RequestBody Map<String, String> body, HttpServletRequest request) {
        String email = extractEmailFromToken(request);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        String name = body.get("name");
        Portfolio portfolio = portfolioService.createPortfolio(user, name);

        return ResponseEntity.ok(new PortfolioDto(portfolio)); // ✅ 무한참조 방지
    }

    // 🔹 내 포트폴리오 목록 조회
    @GetMapping("/list")
    public ResponseEntity<?> getMyPortfolios(HttpServletRequest request) {
        String token = extractToken(request);
        String email = jwtTokenProvider.getUsername(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자 없음"));

        List<Portfolio> portfolios = portfolioRepository.findByUser(user);
        List<PortfolioDto> result = portfolios.stream()
                .map(PortfolioDto::new)
                .toList();

        return ResponseEntity.ok(result); // ✅ 안전한 응답
    }

    // 🔹 주식 구매
    @PostMapping("/add")
    public ResponseEntity<?> buyStock(
            @RequestParam Long portfolioId,
            @RequestParam String isinCd,
            @RequestParam int quantity) {

        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("포트폴리오 없음"));

        StockDto stockDto = stockService.loadStockDataByIsinCd(isinCd);
        portfolioService.addStockInPortfolio(portfolio, stockDto, quantity);

        return ResponseEntity.ok("주식 구매 완료");
    }

    // 🔹 주식 판매
    @PostMapping("/sell")
    public ResponseEntity<?> sellStock(
            @RequestParam Long portfolioId,
            @RequestParam String isinCd,
            @RequestParam int quantity) {

        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("포트폴리오 없음"));

        StockDto stockDto = stockService.loadStockDataByIsinCd(isinCd);
        portfolioService.sellStockInPortfolio(portfolio, stockDto, quantity);

        return ResponseEntity.ok("주식 판매 완료");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePortfolio(@PathVariable Long id, HttpServletRequest request) {
        String email = extractEmailFromToken(request);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "포트폴리오 없음"));

        // 🔐 소유자 확인
        if (!portfolio.getUser().getEmail().equals(email)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "삭제 권한 없음");
        }
        for(PortfolioStock stock : portfolio.getStocks()) {
            portfolioStockRepository.delete(stock);
        }
        portfolioRepository.delete(portfolio);

        return ResponseEntity.ok(Map.of("message", "삭제 완료"));
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<?> getPortfolioInfo(@PathVariable Long id, HttpServletRequest request) {
        String email = extractEmailFromToken(request);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "포트폴리오 없음"));

        // 🔐 소유자 확인
        if (!portfolio.getUser().getEmail().equals(email)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "조회 권한 없음");
        }

        return ResponseEntity.ok(new PortfolioDto(portfolio));
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

    // 토큰 추출 유틸
    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new RuntimeException("토큰 없음");
        }
        return header.substring(7);
    }
}