package springproject.financeproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import springproject.financeproject.domain.Stock;
import springproject.financeproject.domain.StockHistory;

import java.time.LocalDate;
import java.util.List;

public interface StockHistoryRepository extends JpaRepository<StockHistory, Integer> {
    List<StockHistory> findByBasDt(LocalDate basDt);
}