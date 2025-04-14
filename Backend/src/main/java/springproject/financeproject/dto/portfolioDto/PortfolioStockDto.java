package springproject.financeproject.dto.portfolioDto;

import springproject.financeproject.domain.PortfolioStock;

public record PortfolioStockDto(
        String itmsNm,
        String mrktCtg,
        String isinCd,
        int stockNum,
        long cash,
        long mkp,
        double proceeds,
        long earningMoney
) {
    public PortfolioStockDto(PortfolioStock stock) {
        this(
                stock.getItmsNm(),
                stock.getMrktCtg(),
                stock.getIsinCd(),
                stock.getStockNum(),
                stock.getCash(),
                stock.getMkp(),
                stock.getProceeds(),
                stock.getEarningMoney()
        );
    }
}
