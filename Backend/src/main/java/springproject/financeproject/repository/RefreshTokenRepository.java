package springproject.financeproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springproject.financeproject.domain.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
}