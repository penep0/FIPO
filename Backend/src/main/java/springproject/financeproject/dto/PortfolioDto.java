package springproject.financeproject.dto;

import springproject.financeproject.domain.Portfolio;

import java.util.List;

public record PortfolioDto(
        Long id,
        String portfolioName,
        Long totalCash,
        Long earningRate,
        double proceeds,
        List<PortfolioStockDto> stocks // ⬅️ 추가
) {
    public PortfolioDto(Portfolio portfolio) {
        this(
                portfolio.getId(),
                portfolio.getPortfolioName(),
                portfolio.getTotalCash(),
                portfolio.getEarningRate(),
                portfolio.getProceeds(),
                portfolio.getStocks().stream()
                        .map(PortfolioStockDto::new)
                        .toList()
        );
    }
}
