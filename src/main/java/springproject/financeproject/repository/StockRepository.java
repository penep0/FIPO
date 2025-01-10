package springproject.financeproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springproject.financeproject.domain.Stock;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {

    Optional<Stock> findByIsinCd(String isinCd);
    List<Stock> findByItmsNm(String itmsNm);
}
