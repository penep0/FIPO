package springproject.financeproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springproject.financeproject.domain.PortfolioStock;

public interface PortfolioStockRepository extends JpaRepository<PortfolioStock, Long> {
}
