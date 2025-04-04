package springproject.financeproject.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class RefreshToken {

    @Id
    private String email; // 사용자 이메일 (ID로 사용)

    @Column(nullable = false)
    private String token;
}