package springproject.financeproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springproject.financeproject.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
